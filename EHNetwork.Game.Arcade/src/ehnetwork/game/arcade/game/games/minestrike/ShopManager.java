package ehnetwork.game.arcade.game.games.minestrike;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItem;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItemType;
import ehnetwork.game.arcade.game.games.minestrike.items.equipment.DefusalKit;
import ehnetwork.game.arcade.game.games.minestrike.items.equipment.armor.Armor;
import ehnetwork.game.arcade.game.games.minestrike.items.equipment.armor.Helmet;
import ehnetwork.game.arcade.game.games.minestrike.items.equipment.armor.Kevlar;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.FlashBang;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.Grenade;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.HighExplosive;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.Incendiary;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.Molotov;
import ehnetwork.game.arcade.game.games.minestrike.items.grenades.Smoke;
import ehnetwork.game.arcade.game.games.minestrike.items.guns.Gun;
import ehnetwork.game.arcade.game.games.minestrike.items.guns.GunStats;
import ehnetwork.game.arcade.game.games.minestrike.items.guns.Shotgun;

public class ShopManager
{
	private MineStrike Host;

	private HashMap<Player, HashMap<Integer, StrikeItem>> _shop = new HashMap<Player, HashMap<Integer, StrikeItem>>();
	private HashMap<Player, Integer> _money = new HashMap<Player, Integer>();
	private HashSet<Player> _inShop = new HashSet<Player>();

	public ShopManager(MineStrike host)
	{
		Host = host;
	}

	public void enterShop(Player player)
	{
		GameTeam team = Host.GetTeam(player);
		if (team == null)
			return;

		clearShopInventory(player);

		_shop.put(player, new HashMap<Integer, StrikeItem>());

		int slot;

		//Pistols
		slot = 9;
		addItem(team.GetColor() == ChatColor.RED ? new Gun(GunStats.GLOCK_18) : new Gun(GunStats.P2000), player, slot++);
		addItem(new Gun(GunStats.P250), player, slot++);
		addItem(new Gun(GunStats.CZ75), player, slot++);
		addItem(new Gun(GunStats.DEAGLE), player, slot++);

		//Shotgun
		slot = 18;
		addItem(new Shotgun(GunStats.NOVA), player, slot++);
		addItem(new Shotgun(GunStats.XM1014), player, slot++);
		
		//SMG
		addItem(new Gun(GunStats.PPBIZON), player, slot++);
		addItem(new Gun(GunStats.P90), player, slot++);

		//Rifles
		slot = 27;
		addItem(team.GetColor() == ChatColor.RED ? new Gun(GunStats.GALIL) : new Gun(GunStats.FAMAS), player, slot++);
		addItem(team.GetColor() == ChatColor.RED ? new Gun(GunStats.AK47) : new Gun(GunStats.M4A4), player, slot++);
		addItem(team.GetColor() == ChatColor.RED ? new Gun(GunStats.SG553) : new Gun(GunStats.AUG), player, slot++);
		addItem(new Gun(GunStats.SSG08), player, slot++);	
		addItem(new Gun(GunStats.AWP), player, slot++);

		//Grenades
		addItem(new FlashBang(), player, 14);
		addItem(new HighExplosive(), player, 15);
		addItem(new Smoke(), player, 16);
		addItem(team.GetColor() == ChatColor.RED ? new Molotov() : new Incendiary(), player, 17);

		//Gear
		if (team.GetColor() == ChatColor.AQUA)
			addItem(new DefusalKit(), player, 26);

		//Equipment
		addItem(new Helmet(), player, 34);
		addItem(new Kevlar(), player, 35);

		_inShop.add(player);
	}

	public void addItem(StrikeItem item, Player player, int slot)
	{
		player.getInventory().setItem(slot, item.getShopItem(getMoney(player), hasItem(player, item)));
					
		_shop.get(player).put(slot, item);
	}

	public boolean hasItem(Player player, StrikeItem item)
	{
		
		int count = 0;
		for (int i=0 ; i<9 ; i++)
		{
			if (UtilGear.isMat(player.getInventory().getItem(i), item.getSkin()))
				count++;
			
			if (UtilGear.isMat(player.getInventory().getHelmet(), item.getSkin()))
				count++;
			
			if (UtilGear.isMat(player.getInventory().getChestplate(), item.getSkin()))
				count++;
		}
		
		if (count > 0)
		{
			if (item.getType() == StrikeItemType.PRIMARY_WEAPON ||
				item.getType() == StrikeItemType.SECONDARY_WEAPON ||
				item.getType() == StrikeItemType.EQUIPMENT)
					return true;
			
			if (item instanceof Grenade)
			{
				Grenade grenade = (Grenade)item;
				
				if (!grenade.canGiveToPlayer(player))
					return true;
			}
		}
		
		if (item instanceof Kevlar)
		{
			if (Armor.isArmor(player.getInventory().getChestplate()))
			{
				return true;
			}			
		}
		
		if (item instanceof Helmet)
		{
			if (Armor.isArmor(player.getInventory().getHelmet()))
				return true;
		}
		
		return false;
	}
	
	public void clearShopInventory(Player player)
	{
		_shop.remove(player);

		for (int i=9 ; i<36 ; i++)
			player.getInventory().setItem(i, null);
	}

	public int getMoney(Player player)
	{
		if (!_money.containsKey(player))
			_money.put(player, 800);

		return _money.get(player);
	}

	public void addMoney(Player player, int amount, String reason)
	{
		_money.put(player, Math.min(16000, getMoney(player) + amount));

		UtilPlayer.message(player, F.main("Game", "Received " + F.elem(C.cDGreen + "$" + amount) + " for " + reason + "."));
	}

	public void inventoryClick(InventoryClickEvent event)
	{
		event.setCancelled(true);

		Player player = UtilPlayer.searchExact(event.getWhoClicked().getName());
		if (player == null)
			return;

		GameTeam team = Host.GetTeam(player);
		if (team == null)
			return;

		if (!_shop.containsKey(player))
			return;

		if (!_shop.get(player).containsKey(event.getSlot()))
			return;

		//Prevent accidently buying multi
		if (!Recharge.Instance.use(player, "Shop Purchase", 120, false, false))
			return;

		StrikeItem item = _shop.get(player).get(event.getSlot());

		if (item == null)
			return;
		
		if (hasItem(player, item))
			return;
		
		if (getMoney(player) < item.getCost())
		{
			player.playSound(player.getLocation(), Sound.NOTE_BASS, 1f, 1f);
			return;
		}

		//Gun
		if (item instanceof Gun)
		{
			Gun gun = (Gun)item;
			Host.dropSlotItem(player, gun.getSlot());
			gun.giveToPlayer(player, true);
			Host.registerGun(gun, player);
		}

		//Grenade
		else if (item instanceof Grenade)
		{
			Grenade grenade = (Grenade)item;

			if (!grenade.giveToPlayer(player, true))
			{
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 1f, 1f);
				return;
			}

			Host.registerGrenade(grenade, player);
		}

		else if (item instanceof Helmet)
		{
			((Helmet)item).giveToPlayer(player, (team.GetColor() == ChatColor.RED) ? Color.fromRGB(100, 0, 0) : Color.fromRGB(0, 0, 100));
		}

		else if (item instanceof Kevlar)
		{
			((Kevlar)item).giveToPlayer(player, (team.GetColor() == ChatColor.RED) ? Color.fromRGB(100, 0, 0) : Color.fromRGB(0, 0, 100));
		}

		else if (item instanceof DefusalKit)
		{
			item.giveToPlayer(player, 8, false);
		}

		_money.put(player, getMoney(player) - item.getCost());

		enterShop(player);
	}

	public void leaveShop(Player player, boolean showShopItem, boolean wipeMoney)
	{
		_shop.remove(player);

		_inShop.remove(player);
		clearShopInventory(player);

		if (wipeMoney)
			_money.remove(player);

		if (showShopItem)
		{
			player.getInventory().setItem(22, 
					ItemStackFactory.Instance.CreateStack(Material.PAPER, (byte)0, 1, C.cRed + "Cannot Purchase Gear", 
							new String[] 
									{
						C.cWhite + "",
						C.cWhite + "You can only purchase gear when",
						C.cWhite + "you are near your spawn point in",
						C.cWhite + "the first 45 seconds of the round!",
									}));
		}
	}

	public boolean isBuyTime()
	{
		return !UtilTime.elapsed(Host.GetStateTime(), 45000) && Host.InProgress();
	}

	public void update()
	{
		for (Player player : Host.GetPlayers(false))
		{
			GameTeam team = Host.GetTeam(player);

			if (team == null)
			{
				leaveShop(player, false, false);
				continue;
			}

			//Near Shop?
			boolean nearShop = false;
			for (Location loc : team.GetSpawns())
			{
				if (UtilMath.offset(player.getLocation(), loc) < 5)
				{
					nearShop = true;
					break;
				}
			}

			//Leave Shop
			if (_inShop.contains(player) && (!nearShop || !isBuyTime()) || Host.Manager.isSpectator(player) || player.getAllowFlight())
			{	
				leaveShop(player, true, false);
			}
			//Enter Shop
			else if (!_inShop.contains(player) && (nearShop && isBuyTime()) && !Host.Manager.isSpectator(player) && !player.getAllowFlight())
			{
				enterShop(player);
			}
		}
	}
}
