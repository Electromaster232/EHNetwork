package ehnetwork.game.skyclans.clans;

import java.sql.Timestamp;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.blockrestore.BlockRestoreData;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilTime.TimeUnit;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.skyclans.clans.ClansUtility.ClanRelation;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ClansGame extends MiniPlugin
{
	private ClansManager Clans;
	
	public ClansGame(JavaPlugin plugin, ClansManager clans)
	{
		super("Clans Game", plugin);
		
		Clans = clans;
	}	

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		for (Player cur : UtilServer.getPlayers())
			if (Clans.getClanUtility().isSafe(cur.getLocation()))
			{
				long lastDamager = Clans.getCombatManager().Get(cur).GetLastCombat();

				if (!UtilTime.elapsed(lastDamager, 15000))
				{
					UtilPlayer.message(cur, F.main("Safe Zone", "You are not safe for " + 
							F.time(UtilTime.convertString(15000 - (System.currentTimeMillis() - lastDamager), 1, TimeUnit.FIT))));

					Clans.getCondition().Factory().Custom("Unsafe", cur, cur, ConditionType.CUSTOM, 1, 0, false, Material.FIRE, (byte)0, true);
				}
			}
	}
	
	@EventHandler
	public void preventHorseSpawn(CreatureSpawnEvent event)
	{
		if (event.getEntityType() == EntityType.HORSE)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void deductEnergy(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (ClanInfo clan : Clans.getClanMap().values())
		{
			
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void SkillTrigger(SkillTriggerEvent event)
	{
		if (!Clans.getClanUtility().isSafe(event.GetPlayer()))
			return;

		UtilPlayer.message(event.GetPlayer(), F.main("Safe Zone", "You cannot use " + F.skill(event.GetSkillName() + " in " + F.elem("Safe Zone") + ".")));

		event.SetCancelled(true);
	}

	@EventHandler
	public void openClanShop(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (UtilEvent.isAction(event, ActionType.R_BLOCK))
		{
			if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
			{
				Clans.getClanShop().attemptShopOpen(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void BlockBurn(BlockBurnEvent event)
	{
		if (Clans.getClanUtility().isBorderlands(event.getBlock().getLocation()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void BlockSpread(BlockIgniteEvent event)
	{
		if (event.getCause() == IgniteCause.SPREAD)
			if (Clans.getClanUtility().isBorderlands(event.getBlock().getLocation()))
				event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW) 
	public void BlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getType() != Material.LADDER)
			return;
		
		if (Clans.getClanUtility().getAccess(event.getPlayer(), event.getBlock().getLocation()) == ClanRelation.SELF)
			return;
		
		final Block block = event.getBlock();
		
		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Clans.getPlugin(), new Runnable()
		{
			public void run()
			{
				Clans.getBlockRestore().Add(block, 65, block.getData(), 30000);
				
				BlockRestoreData data = Clans.getBlockRestore().GetData(block);
				if (data != null)
				{
					data.setFromId(0);
					data.setFromData((byte)0);
				}
			}
		}, 0);
		
	}

	@EventHandler(priority = EventPriority.LOW) 
	public void BlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled() || event.getPlayer().getWorld().getEnvironment() != Environment.NORMAL || event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		
		String mimic = Clans.Get(event.getPlayer()).getMimic();
		
		if (mimic.length() != 0)
		{
			if (Clans.getClanUtility().searchClanPlayer(event.getPlayer(), mimic, false) != null)
				mimic = C.cGray + " You are currently mimicing " + C.cGold + mimic;
			else
				mimic = "";
		}
		
		//Borderlands
		if (Clans.getClanUtility().isBorderlands(event.getBlock().getLocation()) && event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			//Disallow
			event.setCancelled(true);

			//Inform
			UtilPlayer.message(event.getPlayer(), F.main("Clans", "You can not break " +
					F.elem(ItemStackFactory.Instance.GetName(event.getBlock(), true)) +
					" in " + 
					F.elem("Borderlands") +
					"."));
			return;
		}

		if (Clans.getClanBlocks().canBreak(event.getBlock().getTypeId()))
			return;

		if (Clans.getClanUtility().getAccess(event.getPlayer(), event.getBlock().getLocation()) == ClanRelation.SELF)
		{
			//Disallow Shops
			if (event.getBlock().getType() == Material.ENDER_CHEST || event.getBlock().getType() == Material.ENCHANTMENT_TABLE)
				if (Clans.getClanUtility().isSafe(event.getBlock().getLocation()))
				{
					//Disallow
					event.setCancelled(true);

					//Inform
					UtilPlayer.message(event.getPlayer(), F.main("Clans", "You can not break " +
							F.elem(ItemStackFactory.Instance.GetName(event.getBlock(), true)) +
							" in " + 
							Clans.getClanUtility().getOwnerStringRel(event.getBlock().getLocation(), event.getPlayer().getName()) +
							"."));
				}

			//Disallow Recruit Chest
			if (Clans.getClanUtility().isClaimed(event.getBlock().getLocation()))
				if (event.getBlock().getTypeId() == 54)
				{
					if (Clans.getClanUtility().getRole(event.getPlayer()) == ClanRole.RECRUIT)
					{
						//Disallow
						event.setCancelled(true);

						//Inform
						UtilPlayer.message(event.getPlayer(), F.main("Clans", "Clan Recruits cannot break " +
								F.elem(ItemStackFactory.Instance.GetName(event.getBlock(), true)) + "."));
					}
				}

			//Allow
			return;
		}

		//Disallow
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main("Clans", "You can not break " +
				F.elem(ItemStackFactory.Instance.GetName(event.getBlock(), true)) +
				" in " + 
				Clans.getClanUtility().getOwnerStringRel(event.getBlock().getLocation(), event.getPlayer().getName()) +
				"." + 
				mimic));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player damagee = event.GetDamageePlayer();	
		if (damagee == null)	return;

		if (Clans.getClanUtility().isSafe(damagee))
			event.SetCancelled("Safe Zone");	

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!Clans.getClanUtility().canHurt(damagee, damager))
		{
			//Cancel
			event.SetCancelled("Clans Ally");

			//Inform
			if (damager != null)
			{
				ClanRelation rel = Clans.getRelation(damagee.getName(), damager.getName());
				UtilPlayer.message(damager, F.main("Clans", 
						"You cannot harm " + Clans.getClanUtility().mRel(rel, damagee.getName(), false) + "."));
			}		
		}
	}

	//Block Interact and Placement
	public void Interact(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (player.getWorld().getEnvironment() != Environment.NORMAL)
			return;

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		//Block Interaction
		Location loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
		if (UtilBlock.usable(event.getClickedBlock()))		loc = event.getClickedBlock().getLocation();
		
		//Borderlands
		if (	player.getGameMode() != GameMode.CREATIVE &&
				player.getItemInHand() != null && 
				Clans.getClanBlocks().denyUsePlace(player.getItemInHand().getTypeId()) &&
				Clans.getClanUtility().isBorderlands(event.getClickedBlock().getLocation()))
		{
			//Disallow
			event.setCancelled(true);

			//Inform
			UtilPlayer.message(player, F.main("Clans", "You cannot use/place " +
					F.elem(ItemStackFactory.Instance.GetName(event.getClickedBlock(), true)) +
					" in " + 
					F.elem("Borderlands") +
					"."));

			return;
		}

		ClanRelation access = Clans.getClanUtility().getAccess(player, loc);
		
		//Hoe Return
		if (access != ClanRelation.SELF && !UtilBlock.usable(event.getClickedBlock()))
		{
			if (UtilGear.isHoe(player.getItemInHand()))
			{
				event.setCancelled(true);
				return;
			}
		}

		//Full Access
		if (access == ClanRelation.SELF)
		{
			//Recruits cannot open Chests  // IN OWN CLAIMED LAND
			if (event.getClickedBlock().getTypeId() == 54 && Clans.getClanUtility().getOwner(loc) != null)
			{
				if (Clans.getClanUtility().getRole(player) == ClanRole.RECRUIT)
				{
					//Disallow
					event.setCancelled(true);

					//Inform
					UtilPlayer.message(player, F.main("Clans", "Clan Recruits cannot access " +
							F.elem(ItemStackFactory.Instance.GetName(event.getClickedBlock(), true)) +
							"."));
				}
			}

			//Wilderness Adjacent
			if (	event.getAction() == Action.RIGHT_CLICK_BLOCK && 
					!UtilBlock.usable(event.getClickedBlock()) &&
					player.getItemInHand() != null && 
					Clans.getClanBlocks().denyUsePlace(player.getItemInHand().getTypeId()) &&
					!Clans.getClanUtility().isClaimed(loc))
			{

				String enemy = null;
				boolean self = false;

				for (int x=-1 ; x<=1 ; x++)
					for (int z=-1 ; z<=1 ; z++)
					{
						if (self)
							continue;

						if (x != 0 && z != 0 || x == 0 && z == 0)
							continue;

						Location sideLoc = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);

						if (Clans.getClanUtility().isSelf(player.getName(), sideLoc))
							self = true;

						if (enemy != null)
							continue;

						if (Clans.getClanUtility().getAccess(player, sideLoc) != ClanRelation.SELF)
							enemy = Clans.getClanUtility().getOwnerStringRel(
									new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z), 
									player.getName());
					}

				if (enemy != null && !self)
				{
					//Disallow
					event.setCancelled(true);

					//Inform
					UtilPlayer.message(player, F.main("Clans", "You cannot use/place " +
							F.elem(ItemStackFactory.Instance.GetName(player.getItemInHand(), true)) +
							" next to " + 
							enemy +
							"."));

					return;
				}
			}

			return;
		}

		String mimic = Clans.Get(player).getMimic();
		
		if (mimic.length() != 0)
		{
			if (Clans.getClanUtility().searchClanPlayer(player, mimic, false) != null)
				mimic = C.cGray + " You are currently mimicing " + C.cGold + mimic;
			else
				mimic = "";
		}
		
		//Deny Interaction
		if (Clans.getClanBlocks().denyInteract(event.getClickedBlock().getTypeId()))
		{
			//Block Action
			if (access == ClanRelation.NEUTRAL)
			{
				//Allow Field Chest
				if (event.getClickedBlock().getTypeId() == 54)
					if (Clans.getClanUtility().isSpecial(event.getClickedBlock().getLocation(), "Fields"))
						return;

				//Disallow
				event.setCancelled(true);
				
				//Inform
				UtilPlayer.message(player, F.main("Clans", "You cannot use " +
						F.elem(ItemStackFactory.Instance.GetName(event.getClickedBlock(), true)) +
						" in " + 
						Clans.getClanUtility().getOwnerStringRel(event.getClickedBlock().getLocation(), 
								player.getName()) +
						"." + 
						mimic));
				
				return;
			}
			//Block is not Trust Allowed
			else if (!Clans.getClanBlocks().allowInteract(event.getClickedBlock().getTypeId()) || access != ClanRelation.ALLY_TRUST)
			{
				//Disallow
				event.setCancelled(true);

				//Inform
				UtilPlayer.message(player, F.main("Clans", "You cannot use " +
						F.elem(ItemStackFactory.Instance.GetName(event.getClickedBlock(), true)) +
						" in " + 
						Clans.getClanUtility().getOwnerStringRel(event.getClickedBlock().getLocation(), 
								player.getName()) +
						"." + 
						mimic));

				return;
			}	
		}

		//Block Placement
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (player.getItemInHand().getType() != Material.AIR)
				if (Clans.getClanBlocks().denyUsePlace(player.getItemInHand().getTypeId()))
				{
					//Disallow
					event.setCancelled(true);

					//Inform
					UtilPlayer.message(player, F.main("Clans", "You cannot use/place " +
							F.elem(ItemStackFactory.Instance.GetName(player.getItemInHand(), true)) +
							" in " + 
							Clans.getClanUtility().getOwnerStringRel(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(), 
									player.getName()) +
							"." + 
							mimic));

					return;
				}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Piston(BlockPistonExtendEvent event)
	{
		ClanInfo pistonClan = Clans.getClanUtility().getOwner(event.getBlock().getLocation());

		Block push = event.getBlock();
		for (int i=0 ; i<13 ; i++)
		{
			push = push.getRelative(event.getDirection());

			Block front = push.getRelative(event.getDirection()).getRelative(event.getDirection());

			if (push.getType() == Material.AIR)
				return;

			ClanInfo pushClan = Clans.getClanUtility().getOwner(front.getLocation());

			if (pushClan == null)
				continue;

			if (pushClan.isAdmin())
				continue;

			if (pistonClan == null)
			{
				push.getWorld().playEffect(push.getLocation(), Effect.STEP_SOUND, push.getTypeId());
				event.setCancelled(true);
				return;
			}

			if (pistonClan.equals(pushClan))
				continue;

			push.getWorld().playEffect(push.getLocation(), Effect.STEP_SOUND, push.getTypeId());
			event.setCancelled(true);
			return;
		}			
	}

	@EventHandler
	public void Quit(PlayerQuitEvent event)
	{
		ClanInfo clan = Clans.getClanUtility().getClanByPlayer(event.getPlayer());
		if (clan == null)		return;

		clan.setLastOnline(new Timestamp(System.currentTimeMillis()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Explosion(EntityExplodeEvent event)
	{
		try
		{
			if (event.getEntityType() != EntityType.PRIMED_TNT && event.getEntityType() != EntityType.MINECART_TNT)
				return;
		}
		catch (Exception e)
		{
			return;
		}

		ClanInfo clan = Clans.getClanUtility().getOwner(event.getEntity().getLocation());
		if (clan == null)		return;

		if (!clan.isOnline())
			event.setCancelled(true);
		else
			clan.inform(C.cRed + "Your Territory is under attack!", null);
	}
}
