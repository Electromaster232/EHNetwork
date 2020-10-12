package ehnetwork.game.arcade.managers;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.teleport.event.MineplexTeleportEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.events.PlayerDeathOutEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.GameTeam.PlayerState;
import ehnetwork.game.arcade.kit.perks.event.PerkDestructorBlockEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class GameFlagManager implements Listener
{
	ArcadeManager Manager;

	public GameFlagManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void DamageEvent(CustomDamageEvent event)
	{ 
		Game game = Manager.GetGame();
		if (game == null)	
		{
			event.SetCancelled("Game Null");
			return;
		}

		LivingEntity damagee = event.GetDamageeEntity();
		LivingEntity damager = event.GetDamagerEntity(true);

		if (damagee != null && damagee.getWorld().getName().equals("world"))
		{
			event.SetCancelled("In Lobby");

			if (event.GetCause() == DamageCause.VOID)
				damagee.teleport(Manager.GetLobby().GetSpawn());

			return;
		}
		
		//Damagee Spec
		if (damagee != null && Manager.isSpectator(damagee))
		{
			event.SetCancelled("Damagee Spectator");

			if (damagee.getFireTicks() > 0)
			{
				damagee.setFireTicks(0);
			}

			return;
		}
		
		//Damager Spec
		if (damager != null && Manager.isSpectator(damager))
		{
			event.SetCancelled("Damager Spectator");
			return;
		}

		if (!game.Damage)
		{
			event.SetCancelled("Damage Disabled");
			return;
		}

		if (game.GetState() != GameState.Live)
		{
			event.SetCancelled("Game not Live");
			return; 
		}

		if (damagee != null && damagee instanceof Player && !game.IsAlive((Player)damagee))
		{
			event.SetCancelled("Damagee Not Playing");
			return;
		}

		if (damager != null && damager instanceof Player && !game.IsAlive((Player)damager))
		{
			event.SetCancelled("Damager Not Playing");
			return;
		}

		if (event.GetCause() == DamageCause.FALL && !game.DamageFall)
		{
			event.SetCancelled("Fall Damage Disabled");
			return;
		}
		
		//Entity vs Entity
		if (damagee != null && damager != null)	
		{
			//PvP
			if (damagee instanceof Player && damager instanceof Player)
			{
				if (!Manager.canHurt((Player)damagee, (Player)damager))
				{
					event.SetCancelled("PvP Disabled");
					return;
				}
			}
			//PvE
			else if (damager instanceof Player)
			{
				if (!game.DamagePvE)
				{
					event.SetCancelled("PvE Disabled");
					return;
				}
			}
			//EvP
			else if (damagee instanceof Player)
			{
				if (!game.DamageEvP)
				{
					event.SetCancelled("EvP Disabled");
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void DamageExplosion(CustomDamageEvent event)
	{ 
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_EXPLOSION && event.GetCause() != DamageCause.BLOCK_EXPLOSION)
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (Manager.canHurt(damagee, damager))
			return;

		event.SetCancelled("Allied Explosion");
	}

	

	@EventHandler(priority = EventPriority.LOWEST)
	public void ItemPickupEvent(PlayerPickupItemEvent event)
	{
		Player player = event.getPlayer();

		Game game = Manager.GetGame();

		if (game == null || !game.IsAlive(player) || game.GetState() != GameState.Live)
		{
			event.setCancelled(true);
			return;
		}


		if (game.ItemPickup)
		{
			if (game.ItemPickupDeny.contains(event.getItem().getItemStack().getTypeId()))
			{
				event.setCancelled(true);
			}
		}
		else
		{					
			if (!game.ItemPickupAllow.contains(event.getItem().getItemStack().getTypeId()))
			{
				event.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void ItemDropEvent(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();

		Game game = Manager.GetGame();
		if (game == null || !game.IsAlive(player) || game.GetState() != GameState.Live)
		{
			//Only allow ops in creative
			if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE)	
			{
				event.setCancelled(true);
			}

			return;
		}

		if (game.ItemDrop)
		{
			if (game.ItemDropDeny.contains(event.getItemDrop().getItemStack().getTypeId()))
			{
				event.setCancelled(true);
			}
		}
		else
		{					
			if (!game.ItemDropAllow.contains(event.getItemDrop().getItemStack().getTypeId()))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void InventoryOpen(InventoryOpenEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)
			return;
		
		if (!game.InProgress())
			return;
		
		if (!game.InventoryOpenBlock)
		{
			if (event.getInventory().getType() == InventoryType.ANVIL ||
					event.getInventory().getType() == InventoryType.BEACON ||
					event.getInventory().getType() == InventoryType.BREWING ||
					event.getInventory().getType() == InventoryType.DISPENSER ||
					event.getInventory().getType() == InventoryType.DROPPER ||
					event.getInventory().getType() == InventoryType.ENCHANTING ||
					event.getInventory().getType() == InventoryType.FURNACE ||
					event.getInventory().getType() == InventoryType.HOPPER ||
					event.getInventory().getType() == InventoryType.MERCHANT ||
					event.getInventory().getType() == InventoryType.ENDER_CHEST ||
					event.getInventory().getType() == InventoryType.WORKBENCH)
			{
				event.setCancelled(true);
				event.getPlayer().closeInventory();
			}
		}
		
		
//		if (!game.InventoryOpenChest)
//		{
//			if (event.getInventory().getType() == InventoryType.CHEST)
//			{
//				event.setCancelled(true);
//				event.getPlayer().closeInventory();
//			}
//		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void InventoryClick(InventoryClickEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)
			return;
		
		if (!game.InProgress())
			return;
		
		if (game.InventoryClick)
			return;
		
		Player player = UtilPlayer.searchExact(event.getWhoClicked().getName());
		if (player != null && !game.IsAlive(player))
			return;
	
		if (!game.IsAlive(player))
			return;
		
		if (event.getInventory().getType() == InventoryType.CRAFTING)
		{
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void BlockPlaceEvent(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();

		Game game = Manager.GetGame();
		if (game == null)
		{
			//Only allow ops in creative
			if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE)	
				event.setCancelled(true);
		}
		else
		{
			if (!game.IsAlive(player))
			{
				//Only allow ops in creative
				if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE)	
					event.setCancelled(true);
			}
			// Event Server Allowance
			else if (game.BlockPlaceCreative && player.getGameMode() == GameMode.CREATIVE) 
			{
				return;
			}
			else
			{
				if (game.BlockPlace)
				{
					if (game.BlockPlaceDeny.contains(event.getBlock().getTypeId()))
					{
						event.setCancelled(true);
					}
				}
				else
				{					
					if (!game.BlockPlaceAllow.contains(event.getBlock().getTypeId()))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void BlockBreakEvent(org.bukkit.event.block.BlockBreakEvent event)
	{
		Player player = event.getPlayer();

		Game game = Manager.GetGame();
		if (game == null)
		{
			//Only allow ops in creative
			if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE)	
				event.setCancelled(true);
		}
		else if (game.GetState() == GameState.Live)
		{
			if (!game.IsAlive(player))
			{
				event.setCancelled(true);
			}
			// Event Server Allowance
			else if (game.BlockBreakCreative && player.getGameMode() == GameMode.CREATIVE) 
			{
				return;
			}
			else
			{
				if (game.BlockBreak)
				{
					if (game.BlockBreakDeny.contains(event.getBlock().getTypeId()))
					{
						event.setCancelled(true);
					}

				}
				else
				{
					if (!game.BlockBreakAllow.contains(event.getBlock().getTypeId()))
					{
						event.setCancelled(true);
					}
				}
			}
		}
		else
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PrivateBlockPlace(BlockPlaceEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.PrivateBlocks)
			return;

		if (event.isCancelled())
			return;

		if (!UtilBlock.usable(event.getBlockPlaced()))
			return;

		if (event.getBlockPlaced().getType() != Material.CHEST &&
				event.getBlockPlaced().getType() != Material.FURNACE &&
				event.getBlockPlaced().getType() != Material.BURNING_FURNACE &&
				event.getBlockPlaced().getType() != Material.WORKBENCH)
			return;

		String privateKey = event.getPlayer().getName();

		//Add Empty
		if (!game.PrivateBlockCount.containsKey(privateKey))
			game.PrivateBlockCount.put(privateKey, 0);

		if (game.PrivateBlockCount.get(privateKey) == 4)
			return;

		game.PrivateBlockMap.put(event.getBlockPlaced().getLocation(), event.getPlayer());
		game.PrivateBlockCount.put(event.getPlayer().getName(), game.PrivateBlockCount.get(event.getPlayer().getName()) + 1);

		if (game.PrivateBlockCount.get(privateKey) == 4)
		{
			event.getPlayer().sendMessage(F.main(game.GetName(), "Protected block limit reached."));
		}		
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PrivateBlockPlaceCancel(BlockPlaceEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.PrivateBlocks)
			return;

		if (event.isCancelled())
			return;

		Block block = event.getBlockPlaced();

		if (block.getType() != Material.CHEST)
			return;

		Player player = event.getPlayer();

		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

		for (BlockFace face : faces)
		{
			Block other = block.getRelative(face);

			if (other.getType() != Material.CHEST)
				continue;

			if (!game.PrivateBlockMap.containsKey(other.getLocation()))
				continue;

			Player owner = game.PrivateBlockMap.get(other.getLocation());

			if (player.equals(owner))
				continue;

			//Allow Enemy Raiding
			GameTeam ownerTeam = game.GetTeam(owner);
			GameTeam playerTeam = game.GetTeam(player);

			if (ownerTeam != null && playerTeam != null && !ownerTeam.equals(playerTeam))
				continue;

			//Disallow
			UtilPlayer.message(event.getPlayer(), F.main("Game", 
					"You cannot combine " + 
							F.elem(C.cPurple + ItemStackFactory.Instance.GetName(event.getBlock(), false)) + 
							" with " + F.elem(Manager.GetColor(owner) + owner.getName() + ".")));

			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void PrivateBlockBreak(org.bukkit.event.block.BlockBreakEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.PrivateBlocks)
			return;

		if (event.isCancelled())
			return;

		if (!game.PrivateBlockMap.containsKey(event.getBlock().getLocation()))
			return;

		Player owner = game.PrivateBlockMap.get(event.getBlock().getLocation());
		Player player = event.getPlayer();

		//Same Team (or no team)
		if (owner.equals(player))
		{
			game.PrivateBlockMap.remove(event.getBlock().getLocation());
		}
		else
		{
			//Allow Enemy Raiding
			GameTeam ownerTeam = game.GetTeam(owner);
			GameTeam playerTeam = game.GetTeam(player);

			if (ownerTeam != null && playerTeam != null && !ownerTeam.equals(playerTeam))
				return;

			//Disallow
			UtilPlayer.message(event.getPlayer(), F.main("Game", 
					F.elem(C.cPurple + ItemStackFactory.Instance.GetName(event.getBlock(), false)) + 
					" belongs to " + F.elem(Manager.GetColor(owner) + owner.getName() + ".")));

			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PrivateBlockUse(PlayerInteractEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.PrivateBlocks)
			return;

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (!UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getClickedBlock().getType() != Material.CHEST &&
				event.getClickedBlock().getType() != Material.FURNACE &&
				event.getClickedBlock().getType() != Material.BURNING_FURNACE)
			return;

		if (!game.PrivateBlockMap.containsKey(event.getClickedBlock().getLocation()))
			return;

		Player owner = game.PrivateBlockMap.get(event.getClickedBlock().getLocation());
		
		if (!game.IsAlive(owner))
			return;
		
		Player player = event.getPlayer();

		if (owner.equals(player))
		{
			return;
		}
		else
		{
			//Allow Enemy Raiding
			GameTeam ownerTeam = game.GetTeam(owner);
			GameTeam playerTeam = game.GetTeam(player);

			if (ownerTeam != null && playerTeam != null && !ownerTeam.equals(playerTeam))
				return;

			//Disallow
			UtilPlayer.message(event.getPlayer(), F.main("Game", 
					F.elem(C.cPurple + ItemStackFactory.Instance.GetName(event.getClickedBlock(), false)) + 
					" belongs to " + F.elem(Manager.GetColor(owner) + owner.getName() + ".")));

			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PrivateBlockCrumble(PerkDestructorBlockEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.PrivateBlocks)
			return;

		if (event.isCancelled())
			return;

		if (!game.PrivateBlockMap.containsKey(event.getBlock().getLocation()))
			return;

		Player owner = game.PrivateBlockMap.get(event.getBlock().getLocation());
		Player player = event.getPlayer();

		//Same Team (or no team)
		if (owner.equals(player))
		{
			game.PrivateBlockMap.remove(event.getBlock().getLocation());
		}
		else
		{
			//Allow Enemy Raiding
			GameTeam ownerTeam = game.GetTeam(owner);
			GameTeam playerTeam = game.GetTeam(player);

			if (ownerTeam != null && playerTeam != null && !ownerTeam.equals(playerTeam))
				return;

			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerDeath(PlayerDeathEvent event)
	{
		final Game game = Manager.GetGame();
		if (game == null)	return;

		final Player player = event.getEntity();
		
		//Visual
		Manager.GetCondition().Factory().Blind("Ghost", player, player, 2, 0, false, false, false);

		player.setFireTicks(0);
		player.setFallDistance(0);

		//Drop Items
		if (game.DeathDropItems)
			for (ItemStack stack : event.getDrops())
				player.getWorld().dropItem(player.getLocation(), stack);
		event.getDrops().clear();

		//DEATH OUT
		if (game.GetState() == GameState.Live && game.DeathOut)
		{
			//Event
			PlayerDeathOutEvent outEvent = new PlayerDeathOutEvent(game, player);
			UtilServer.getServer().getPluginManager().callEvent(outEvent);

			if (!outEvent.isCancelled())
			{
				game.SetPlayerState(player, PlayerState.OUT);
			}
		}

		//RESPAWN 
		if (game.DeathSpectateSecs <= 0 && (game.GetTeam(player) == null || game.GetTeam(player).GetRespawnTime() <= 0))
		{
			//Teleport
			if (game.AutomaticRespawn && game.IsAlive(player))
			{
				game.RespawnPlayer(player);
			} 
			else
			{
				Manager.addSpectator(player, true);
			}

			Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					player.setFireTicks(0);
					player.setVelocity(new Vector(0,0,0));
				}
			}, 0);
		}
		//TIMER
		else
		{
			double time = game.DeathSpectateSecs;
			if (game.GetTeam(player) != null)
				if (game.GetTeam(player).GetRespawnTime() > time)
					time = game.GetTeam(player).GetRespawnTime();
			
			UtilInv.Clear(player);
			Manager.GetCondition().Factory().Blind("Ghost", player, player, 1.5, 0, false, false, false);
			Manager.GetCondition().Factory().Cloak("Ghost", player, player, time, false, false);
			player.setAllowFlight(true);
			player.setFlying(true);
			((CraftPlayer)player).getHandle().spectating = true;
			((CraftPlayer)player).getHandle().k = false;
			
			for (int i=0 ; i<9 ; i++)
				player.getInventory().setItem(i, new ItemStack(Material.SKULL));
			
			UtilAction.velocity(player, new Vector(0,0,0), 1, true, 0.4, 0, 1, true);
			
			if (!game.IsAlive(player))
			{
				Manager.addSpectator(player, true);
				return;
			}
			
			UtilPlayer.message(player, C.cWhite + C.Bold + "You will respawn in " + time + " seconds...");
			UtilTextMiddle.display(null, "Respawning in " + time + " seconds...", 5, 40, 5, player);
	
			Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					//Teleport
					if (game.IsAlive(player))
					{
						game.RespawnPlayer(player);
					}
					else
					{
						Manager.addSpectator(player, true);
					}
					 
					player.setFireTicks(0);
					player.setVelocity(new Vector(0,0,0));
				}
			}, (int)(time * 20d));
		}	
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		//Drop Items
		if (game.QuitDropItems)
			if (game.IsLive())
				if (game.IsAlive(event.getPlayer()))
					UtilInv.drop(event.getPlayer(), true);
			
		//Remove Kit
		game.RemoveTeamPreference(event.getPlayer());
		game.GetPlayerKits().remove(event.getPlayer());
		game.GetPlayerGems().remove(event.getPlayer());
		
		if (!game.QuitOut)
			return;
		
		GameTeam team = game.GetTeam(event.getPlayer());
		
		if (team != null)
		{
			if (game.InProgress())
				team.SetPlayerState(event.getPlayer(), PlayerState.OUT);
			else
				team.RemovePlayer(event.getPlayer());
		}
	}
	
	@EventHandler
	public void PlayerMoveCancel(PlayerMoveEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null || game.GetState() != GameState.Prepare)
			return;

		if (!game.PrepareFreeze)
			return;
		
		if (!game.IsAlive(event.getPlayer()))
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) <= 0)
			return;

		event.getFrom().setPitch(event.getTo().getPitch());
		event.getFrom().setYaw(event.getTo().getYaw());

		event.setTo(event.getFrom());
	}

	@EventHandler
	public void PlayerHealthFoodUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Game game = Manager.GetGame();
		
		//Not Playing
		for (Player player : UtilServer.getPlayers())
		{
			if (game == null || game.GetState() == GameState.Recruit || !game.IsAlive(player))
			{
				player.setMaxHealth(20);
				player.setHealth(20);
				player.setFoodLevel(20);
			}
		}

		if (game == null || !game.IsLive())
			return;

		if (game.HungerSet != -1)
			for (Player player : game.GetPlayers(true))
				player.setFoodLevel(game.HungerSet);

		if (game.HealthSet != -1)
			for (Player player : game.GetPlayers(true))
				player.setHealth(game.HealthSet);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerBoundaryCheck(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Game game = Manager.GetGame();
		if (game == null || game.GetState() != GameState.Live)
			return;
		
		for (Player player : UtilServer.getPlayers())
		{
			if (!game.isInsideMap(player) && game.IsAlive(player))
			{	
				//Riding a Projectile, edgecase
				if (player.getVehicle() != null && player.getVehicle() instanceof Projectile)
				{
					player.getVehicle().remove();
					player.leaveVehicle();
					((CraftPlayer)player).getHandle().spectating = false;
				}
				
				if (!Manager.IsAlive(player) || ((CraftPlayer)player).getHandle().spectating)
				{
					player.teleport(game.GetSpectatorLocation());
				}
				else
				{
					if (!game.WorldBoundaryKill)
					{
						UtilPlayer.message(player, C.cRed + C.Bold + "WARNING: " + C.cWhite + C.Bold + "RETURN TO PLAYABLE AREA!");

						if (game.GetType() != GameType.Gravity)
						{
							if (player.getLocation().getY() > game.WorldData.MaxY)
								UtilAction.velocity(player, UtilAlg.getTrajectory2d(player.getLocation(), game.GetSpectatorLocation()), 1, true, 0, 0, 10, true);
							else
								UtilAction.velocity(player, UtilAlg.getTrajectory2d(player.getLocation(), game.GetSpectatorLocation()), 1, true, 0.4, 0, 10, true);
						}
						
						Manager.GetDamage().NewDamageEvent(player, null, null, 
								DamageCause.VOID, 4, false, false, false,
								"Border", "Border Damage");

						player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 1f);
						player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 1f);	
					}
					else
					{
						Manager.GetDamage().NewDamageEvent(player, null, null, 
								DamageCause.VOID, 9001, false, false, false,
								"Border", "Border Damage");
					}		
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void WorldCreature(CreatureSpawnEvent event)
	{	
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.CreatureAllow && !game.CreatureAllowOverride)
		{
			if (game.WorldData != null)
			{
				if (game.WorldData.World != null)
				{
					if (event.getLocation().getWorld().equals(game.WorldData.World))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	} 
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void StaffDisqualify(MineplexTeleportEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (Manager.GetClients().Get(event.getPlayer()).GetRank().Has(Rank.DEVELOPER))
			return;
		
		Game game = Manager.GetGame();
		if (game == null)	return;
		
		if (!game.IsLive())
			return;
		
		if (!game.TeleportsDisqualify)
			return;

		if (!game.IsAlive(event.getPlayer()))
			return;

		//Remove Kit
		game.RemoveTeamPreference(event.getPlayer());
		game.GetPlayerKits().remove(event.getPlayer());
		game.GetPlayerGems().remove(event.getPlayer());
		
		//Remove Team
		GameTeam team = game.GetTeam(event.getPlayer());
		if (team != null)
		{
			if (game.InProgress())
				team.SetPlayerState(event.getPlayer(), PlayerState.OUT);
			else
				team.RemovePlayer(event.getPlayer());
		}
		
		Manager.addSpectator(event.getPlayer(), false);
	}

	@EventHandler
	public void WorldTime(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.WorldTimeSet != -1)
		{
			if (game.WorldData != null)
			{
				if (game.WorldData.World != null)
				{
					game.WorldData.World.setTime(game.WorldTimeSet);
				}
			}
		}
	}
	
	@EventHandler
	public void WorldWeather(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.WorldWeatherEnabled)
		{
			if (game.WorldData != null)
			{
				if (game.WorldData.World != null)
				{
					game.WorldData.World.setStorm(false);
					game.WorldData.World.setThundering(false);
				}
			}
		}
	}

	@EventHandler
	public void WorldWaterDamage(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!game.IsLive())
			return;		
		
		if (game.WorldWaterDamage <= 0)
		{
			if (!game.WorldData.GetCustomLocs("WATER_DAMAGE").isEmpty())
			{
				game.WorldWaterDamage = 4;
			}
			else
			{
				return;
			}
		}

		for (GameTeam team : game.GetTeamList())
			for (Player player : team.GetPlayers(true))
				if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getRelative(BlockFace.UP).getTypeId() == 8 || 
					player.getLocation().getBlock().getTypeId() == 9 || player.getLocation().getBlock().getRelative(BlockFace.UP).getTypeId() == 9)
				{
					//Damage Event
					Manager.GetDamage().NewDamageEvent(player, null, null, 
							DamageCause.DROWNING, game.WorldWaterDamage, true, false, false,
							"Water", "Water Damage");

					player.getWorld().playSound(player.getLocation(),
							Sound.SPLASH, 0.8f,
							1f + (float) Math.random() / 2);
				}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void WorldSoilTrample(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.PHYSICAL)
			return;
	
		Game game = Manager.GetGame();
		if (game == null)	return;
		
		if (game.WorldSoilTrample)
			return;

		if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.SOIL)
			return;

		event.setCancelled(true);
	}
	
	@EventHandler
	public void WorldBlockBurn(BlockBurnEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.WorldBlockBurn)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void WorldFireSpread(BlockIgniteEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.WorldFireSpread)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void WorldLeavesDecay(LeavesDecayEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.WorldLeavesDecay)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void SpectatorMessage(UpdateEvent event)
	{
		if (Manager.IsTournamentServer())
			return;
		
		if (Manager.GetGame() == null)
			return;
				 
		if (!Manager.GetGame().AnnounceStay)
			return;
		
		if (!Manager.GetGame().IsLive())
			return;		
		
		if (event.getType() != UpdateType.SEC)
			return;
		
		if (Manager.GetGame().GetType() == GameType.MineStrike)
			return;
	
		for (Player player : UtilServer.getPlayers())
		{
			if (Manager.IsAlive(player))
				continue;
			
			if (Recharge.Instance.use(player, "Dont Quit Message", 30000, false, false))
			{
				UtilPlayer.message(player, " ");
				UtilPlayer.message(player, C.cWhite + C.Bold + "You are out of the game, but " + C.cGold + C.Bold + "DON'T QUIT" + C.cWhite + C.Bold + "!");
				UtilPlayer.message(player, C.cWhite + C.Bold + "The next game will be starting soon...");
			}
		}
	}
	
	@EventHandler
	public void AntiHackStrict(GameStateChangeEvent event) 
	{
		if (event.GetState() == GameState.Prepare || event.GetState() == GameState.Live)
			AntiHack.Instance.setStrict(event.GetGame().StrictAntiHack);
		
		else
			AntiHack.Instance.setStrict(true);
	}
	
	@EventHandler
	public void PlayerKillCommandCancel(PlayerCommandPreprocessEvent event)
	{
		if (Manager.GetGame() == null)
			return;
				 
		if (!Manager.GetGame().DisableKillCommand)
			return;
		
		if (event.getMessage().toLowerCase().startsWith("/kill"))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "Suicide is disabled."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void versionUpdateCheck(UpdateEvent event)
	{
		if (Manager.GetGame() == null)
			return;
				 
		if (!Manager.GetGame().VersionRequire1_8)
			return;
		
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!UtilPlayer.is1_8(player))
				versionKick(player);
		}
	}
	
	@EventHandler
	public void versionJoinCheck(PlayerJoinEvent event)
	{
		if (Manager.GetGame() == null)
			return;
				 
		if (!Manager.GetGame().VersionRequire1_8)
			return;
		
		if (!UtilPlayer.is1_8(event.getPlayer()))
			versionKick(event.getPlayer());
	}
	
	public void versionKick(Player player)
	{
		if (Manager.GetGame() == null)
			return;
				 
		if (Manager.GetGame().GetType().getResourcePackUrl() == null)
			return;
		
		UtilPlayer.message(player, "  ");
		UtilPlayer.message(player, C.cGold + C.Bold + Manager.GetGame().GetType().GetName() + " requires you to be using Minecraft 1.8!");
		UtilPlayer.message(player, "  ");

		player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
		Manager.GetPortal().sendPlayerToServer(player, "Lobby");
	}
	
	@EventHandler
	public void resourceInform(PlayerJoinEvent event)
	{
		if (Manager.GetGame() == null)
			return;
				 
		if (Manager.GetGame().GetType().getResourcePackUrl() == null)
			return;
		
		UtilTextMiddle.display(C.cGold + C.Bold + Manager.GetGame().GetType().GetName(), "Make sure you accept the Resource Pack", 20, 120, 20, event.getPlayer());
	}
}
