package ehnetwork.core.achievement;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.command.StatsCommand;
import ehnetwork.core.achievement.ui.AchievementShop;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.core.stats.event.StatChangeEvent;

public class AchievementManager extends MiniPlugin
{
	private StatsManager _statsManager;

	private AchievementShop _shop;
	private int _interfaceSlot = 7;
	private boolean _giveInterfaceItem = false;

	private NautHashMap<String, NautHashMap<Achievement, AchievementLog>> _log = new NautHashMap<String, NautHashMap<Achievement, AchievementLog>>();
	
	private boolean _shopEnabled = true;

	public AchievementManager(StatsManager statsManager, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Achievement Manager", statsManager.getPlugin());

		_statsManager = statsManager;
		_shop = new AchievementShop(this, _statsManager, clientManager, donationManager, "Achievement");
	}

	public AchievementData get(Player player, Achievement type)
	{
		return get(player.getName(), type);
	}

	public AchievementData get(String playerName, Achievement type)
	{
		int exp = 0;

		for (String stat : type.getStats())
		{
			exp += _statsManager.Get(playerName).getStat(stat);
		}

		return type.getLevelData(exp);
	}

	@EventHandler
	public void informLevelUp(StatChangeEvent event)
	{
		Player player = UtilPlayer.searchExact(event.getPlayerName());
		if (player == null)
			return;

		for (Achievement type : Achievement.values())
		{
			for (String stat : type.getStats())
			{
				if (stat.equalsIgnoreCase(event.getStatName()))
				{
					if (!_log.containsKey(player.getName()))
						_log.put(player.getName(), new NautHashMap<Achievement, AchievementLog>());

					//Record that achievement has leveled up
					if (type.getLevelData(event.getValueAfter()).getLevel() > type.getLevelData(event.getValueBefore()).getLevel())
					{
						//Add new
						if (!_log.get(player.getName()).containsKey(type))
						{
							_log.get(player.getName()).put(type, new AchievementLog(event.getValueAfter() - event.getValueBefore(), true));
						}
						//Edit previous
						else
						{
							AchievementLog log = _log.get(player.getName()).get(type);
							log.Amount += event.getValueAfter() - event.getValueBefore();
							log.LevelUp = true;
						}

					}
					//Record that there has been changes in this Achievement
					else if (!_log.get(player.getName()).containsKey(type))
					{
						//Add new
						if (!_log.get(player.getName()).containsKey(type))
						{
							_log.get(player.getName()).put(type, new AchievementLog(event.getValueAfter() - event.getValueBefore(), false));
						}
						//Edit previous
						else
						{
							AchievementLog log = _log.get(player.getName()).get(type);
							log.Amount += event.getValueAfter() - event.getValueBefore();
						}
					}
				}
			}
		}
	}

	@Override
	public void addCommands()
	{
		addCommand(new StatsCommand(this));
	}

	public void openShop(Player player)
	{
		_shop.attemptShopOpen(player);
	}

	public void openShop(Player player, Player target)
	{
		_shop.attemptShopOpen(player, target);
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_log.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		if (_giveInterfaceItem)
		{
			giveInterfaceItem(event.getPlayer());
		}
	}

	public void clearLog(Player player)
	{
		_log.remove(player.getName());
	}

	public NautHashMap<Achievement, AchievementLog> getLog(Player player)
	{
		return _log.remove(player.getName());
	}

	public void setGiveInterfaceItem(boolean giveInterfaceItem)
	{
		_giveInterfaceItem = giveInterfaceItem;
	}

	public void giveInterfaceItem(Player player)
	{
		if (!UtilGear.isMat(player.getInventory().getItem(_interfaceSlot), Material.SKULL_ITEM))
		{
			ItemStack item = ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte) 3, 1, ChatColor.RESET + C.cGreen + "/stats");
			SkullMeta meta = ((SkullMeta) item.getItemMeta());
			meta.setOwner(player.getName());
			item.setItemMeta(meta);

			player.getInventory().setItem(_interfaceSlot, item);

			UtilInv.Update(player);
		}
	}

	@EventHandler
	public void openShop(PlayerInteractEvent event)
	{
		if (!_shopEnabled)
			return;
		
		if (event.hasItem() && event.getItem().getType() == Material.SKULL_ITEM)
		{
			event.setCancelled(true);

			openShop(event.getPlayer());
		}
	}

	public boolean hasCategory(Player player, Achievement[] required)
	{
		if (required == null || required.length == 0)
			return false;
		
		for (Achievement cur : required)
		{
			if (get(player, cur).getLevel() < cur.getMaxLevel())
				return false;
		}
		
		return true;
	}
	
	public int getMineplexLevelNumber(Player sender, Rank rank)
	{
		int level = get(sender, Achievement.GLOBAL_MINEPLEX_LEVEL).getLevel();

		if (sender.getName().equalsIgnoreCase("B2_mp"))
			return 101;

		if (rank.Has(Rank.MODERATOR))
			level = Math.max(level, 5);
		if (rank.Has(Rank.SNR_MODERATOR))
			level = Math.max(level, 15);
		if (rank.Has(Rank.JNR_DEV))
			level = Math.max(level, 25);
		if (rank.Has(Rank.ADMIN))
			level = Math.max(level, 30 + get(sender, Achievement.GLOBAL_GEM_HUNTER).getLevel());
		if (rank.Has(Rank.OWNER))
			level = Math.max(level, 50 + get(sender, Achievement.GLOBAL_GEM_HUNTER).getLevel());

		if (sender.getName().equalsIgnoreCase("Phinary"))
			level = -level;
		
		return level;
	}

	public String getMineplexLevel(Player sender, Rank rank)
	{		
		return Achievement.getExperienceString(getMineplexLevelNumber(sender, rank)) + " " + ChatColor.RESET;
	}
	
	public void setShopEnabled(boolean var)
	{
		_shopEnabled = var;
	}
}
