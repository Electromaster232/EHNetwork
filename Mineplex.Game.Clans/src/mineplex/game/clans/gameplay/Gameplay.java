package mineplex.game.clans.gameplay;

import java.util.HashMap;
import java.util.HashSet;

import mineplex.core.MiniPlugin;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.minecraft.game.core.damage.DamageManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.game.clans.clans.ClansManager;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Gameplay extends MiniPlugin
{
	private ClansManager _clansManager;
	private BlockRestore _blockRestore;
	private DamageManager _damageManager;
	
	private HashMap<Block, Long> _bucketWater = new HashMap<Block, Long>();

	public Gameplay(JavaPlugin plugin, ClansManager clansManager, BlockRestore blockRestore, DamageManager damageManager) 
	{
		super("PvP Gameplay", plugin);
	
		_clansManager = clansManager;
		_blockRestore = blockRestore;
		_damageManager = damageManager;
	}

	@EventHandler
	public void BucketEmpty(PlayerBucketEmptyEvent event)
	{
		event.setCancelled(true);

		Block block = event.getBlockClicked().getRelative(event.getBlockFace());

		if (event.getBucket() == Material.WATER_BUCKET)
		{
			block.setTypeIdAndData(8, (byte)1, true);
			_blockRestore.Add(event.getBlockClicked().getRelative(event.getBlockFace()), 8, (byte)0, 1000);
			_bucketWater.put(block, System.currentTimeMillis());
		}

		if (event.getBucket() == Material.LAVA_BUCKET)
		{
			block.setTypeIdAndData(10, (byte)6, true);
			_blockRestore.Add(event.getBlockClicked().getRelative(event.getBlockFace()), 10, (byte)0, 2000);
		}


		event.getPlayer().setItemInHand(ItemStackFactory.Instance.CreateStack(Material.BUCKET));	
		UtilInv.Update(event.getPlayer());
	}

	@EventHandler
	public void BucketFill(PlayerBucketFillEvent event)
	{
		event.setCancelled(true);

		if (event.getItemStack().getType() == Material.WATER_BUCKET)
			if (!_bucketWater.containsKey(event.getBlockClicked()))
				event.getPlayer().setItemInHand(ItemStackFactory.Instance.CreateStack(Material.WATER_BUCKET));

		UtilInv.Update(event.getPlayer());
	}

	@EventHandler
	public void BucketWaterExpire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		HashSet<Block> remove = new HashSet<Block>();

		for (Block cur : _bucketWater.keySet())
			if (UtilTime.elapsed(_bucketWater.get(cur), 2000))
				remove.add(cur);

		for (Block cur : remove)
			_bucketWater.remove(cur);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void ObsidianCancel(BlockPlaceEvent event)
	{
		if (event.getBlock().getType() == Material.OBSIDIAN)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot place " + F.item("Obsidian") + "."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void CommandPlace(BlockPlaceEvent event)
	{
		if (event.getBlock().getType() == Material.COMMAND || 
			event.getBlock().getType() == Material.NOTE_BLOCK || 
			event.getBlock().getType() == Material.REDSTONE_LAMP_ON)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot place " + F.item("Proximity Devices") + "."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void WebBreak(BlockDamageEvent event)
	{
		if (event.isCancelled())
			return;
	
		if (event.getBlock().getType() == Material.WEB)
			event.setInstaBreak(true);
	}

	@EventHandler
	public void LapisPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getBlock().getType() != Material.LAPIS_BLOCK)
			return;
		
		event.setCancelled(true);	
		
		UtilInv.remove(event.getPlayer(), Material.LAPIS_BLOCK, (byte)0, 1);
		
		final Block block = event.getBlock();
		
		_plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
		{
			public void run()
			{
				block.setType(Material.WATER);
				block.setData((byte)0);
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 8);
				block.getWorld().playSound(block.getLocation(), Sound.SPLASH, 2f, 1f);
			}
		}, 0);
	}
	
	@EventHandler
	public void EnderChestBreak(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getType() != Material.ENDER_CHEST)
			return;
	
		event.setCancelled(true);
		
		event.getBlock().setTypeId(0);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5), ItemStackFactory.Instance.CreateStack(Material.ENDER_CHEST));
	}

	@EventHandler
	public void IronDoor(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		if (event.getClickedBlock().getTypeId() != 71)
			return;

		Block block = event.getClickedBlock();

		//Knock
		if (event.isCancelled())
		{
			if (!Recharge.Instance.use(event.getPlayer(), "Door Knock", 500, false, false))
				return;

			block.getWorld().playEffect(block.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
		}

		//Open
		else
		{
			if (block.getData() >= 8)
				block = block.getRelative(BlockFace.DOWN);

			if (block.getData() < 4)	block.setData((byte)(block.getData()+4), true);
			else						block.setData((byte)(block.getData()-4), true);

			//Effect
			block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
		}
	}

	@EventHandler
	public void BrewingDisable(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		if (event.getClickedBlock().getTypeId() != 117)
			return;

		event.setCancelled(true);
	}
	
	@EventHandler
	public void BrewingBreak(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getType() != Material.BREWING_STAND)
			return;
	
		event.setCancelled(true);
		
		event.getBlock().setTypeId(0);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5), ItemStackFactory.Instance.CreateStack(Material.BREWING_STAND));
	}

	@EventHandler
	public void AnvilDisable(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		if (event.getClickedBlock().getType() != Material.ANVIL)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void BonemealCancel(PlayerInteractEvent event)
	{	
		if (!UtilEvent.isAction(event, ActionType.R))
			return;

		Player player = event.getPlayer();

		if (player.getItemInHand() == null)
			return;

		if (player.getItemInHand().getType() != Material.INK_SACK)
			return;

		if (player.getItemInHand().getData() == null)
			return;

		if (player.getItemInHand().getData().getData() != 15)
			return;

		event.setCancelled(true);
	}
	
	@EventHandler
	public void WildfireSpread(BlockBurnEvent event)
	{
		if (event.isCancelled())
			return;
		
		event.setCancelled(true);
		
		for (int x=-1 ; x<=1 ; x++)
			for (int y=-1 ; y<=1 ; y++)
				for (int z=-1 ; z<=1 ; z++)
				{
					//Self
					if (x == 0 && y == 0 && z == 0)
					{
						event.getBlock().setType(Material.FIRE);
						
						if (event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS)
							event.getBlock().getRelative(BlockFace.DOWN).setType(Material.DIRT);
						
						return;
					}
					
					Block block = event.getBlock().getRelative(x, y, z);
					
					if (block.getRelative(BlockFace.DOWN).getType() == Material.GRASS)
						block.getRelative(BlockFace.DOWN).setType(Material.DIRT);
					
					//Surroundings
					if (!(
							(x == 0 && y == 0) ||
							(x == 0 && z == 0) ||
							(y == 0 && z == 0)
							))
						continue;

					if (block.getTypeId() == 0)
						block.setType(Material.FIRE);
				}
	}
	
	@EventHandler
	public void WildfireDirt(BlockIgniteEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS)
			event.getBlock().getRelative(BlockFace.DOWN).setType(Material.DIRT);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void WildfireCancel(BlockIgniteEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getBiome() == Biome.JUNGLE || event.getBlock().getBiome() == Biome.JUNGLE_HILLS)
			if (event.getCause() == IgniteCause.SPREAD)
				event.setCancelled(true);
	}
	
	/*
	@EventHandler (priority = EventPriority.HIGHEST)
	public void MoneyLossSteal(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.GetEvent().getEntity();
		
		int balance = Clients().Get(player).Game().GetEconomyBalance();
		
		int lose = (int) (0.04 * balance);
		
		//Balance
		Clients().Get(player).Game().SetEconomyBalance(balance - lose);

		CombatLog log = event.GetLog();
		if (log.GetKiller() != null)
		{
			//Inform
			UtilPlayer.message(UtilPlayer.searchExact(log.GetKiller().getName()), F.main("Death", "You stole " + F.count((lose) + " Coins") + " from " + F.name(player.getName()) + "."));
			
			//Inform
			UtilPlayer.message(player, F.main("Death", "You lost " + F.count((lose) + " Coins") + " to " + F.name(log.GetKiller().getName()) + "."));
		}
		else
		{
			//Inform
			UtilPlayer.message(player, F.main("Death", "You lost " + F.count((lose) + " Coins") + " for dying."));
		}
	}
	*/
	
	@EventHandler
	public void SpawnDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.FALL)
			return;

		if (!_clansManager.getClanUtility().isSpecial(event.GetDamageeEntity().getLocation(), "Spawn"))
			return;

		event.SetCancelled("Spawn Fall");
	}

	@EventHandler
	public void Repair(PlayerInteractEvent event)
	{
		if (event.getClickedBlock() == null)
			return;

		if (event.getClickedBlock().getType() != Material.ANVIL)
			return;
		
		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		Player player = event.getPlayer();
		
		if (UtilMath.offset(player.getLocation(), event.getClickedBlock().getLocation()) > 2)
		{
			UtilPlayer.message(player, F.main("Repair", "You are too far from the " + F.item("Anvil") + "."));
			return;
		}

		if (player.getItemInHand() == null)
			return;

		ItemStack item = player.getItemInHand();
		
		if (item.getDurability() <= 0)
		{
			UtilPlayer.message(player, F.main("Repair", "Your " + F.item(item == null ? ChatColor.YELLOW + "Hand" : item.getItemMeta().getDisplayName()) + " does not need repairs."));
			return;
		}
		
		if (!UtilGear.isRepairable(item))
		{
			UtilPlayer.message(player, F.main("Repair", "You cannot repair " + F.item(item.getItemMeta().getDisplayName()) + "."));
			return;
		}

		String 	creator = ItemStackFactory.Instance.GetLoreVar(item, "Owner");

		if (creator != null)
		{
			if (creator.length() > 2)
				creator = creator.substring(2, creator.length());
			
			if (!creator.equals(player.getName()))
			{
				UtilPlayer.message(player, F.main("Repair", "You cannot repair " + F.item(item.getItemMeta().getDisplayName()) + " by " + F.name(creator) + "."));	
				return;
			}
		}

		//Repair!
		UtilPlayer.message(player, F.main("Repair", "You repaired " + F.item(item.getItemMeta().getDisplayName()) + "."));
		item.setDurability((short)0);
		UtilInv.Update(player);

		//Break
		if (Math.random() > 0.85)
			event.getClickedBlock().setData((byte) (event.getClickedBlock().getData() + 4));
		
		if (event.getClickedBlock().getData() >= 12)
		{
			player.getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, 145);
			event.getClickedBlock().setTypeIdAndData(0, (byte)0, true);
		}

		//Record
		int repairs = 1 + ItemStackFactory.Instance.GetLoreVar(item, "Repaired", 0);

		ItemStackFactory.Instance.SetLoreVar(item, "Repaired", "" + repairs);
		
		//Effect
		player.playSound(player.getLocation(), Sound.ANVIL_USE, 1f, 1f);
	}

	public DamageManager getDamageManager()
	{
		return _damageManager;
	}
}
