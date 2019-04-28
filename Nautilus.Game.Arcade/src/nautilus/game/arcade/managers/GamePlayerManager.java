package nautilus.game.arcade.managers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mineplex.core.common.CurrencyType;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTabTitle;
import mineplex.core.donation.Donor;
import mineplex.core.recharge.Recharge;
import mineplex.core.shop.page.ConfirmationPage;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.PlayerStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.shop.ArcadeShop;
import nautilus.game.arcade.shop.KitPackage;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;

public class GamePlayerManager implements Listener
{
	ArcadeManager Manager;

	public GamePlayerManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerDeath(CombatDeathEvent event)
	{
		//Don't actually die
		event.GetEvent().getEntity().setHealth(20);

		//Dont display message
		if (Manager.GetGame() != null)
			event.SetBroadcastType(Manager.GetGame().GetDeathMessageType());

		//Colors
		if (event.GetLog().GetKiller() != null)
		{
			Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
			if (player != null)
				event.GetLog().SetKillerColor(Manager.GetColor(player)+"");
		}

		if (event.GetEvent().getEntity() instanceof Player)
		{
			Player player = (Player)event.GetEvent().getEntity();
			if (player != null)
				event.GetLog().SetKilledColor(Manager.GetColor(player)+"");
		}
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		//Player List
		UtilTabTitle.setHeaderAndFooter(player, 
				Manager.GetGame() != null ? C.cGold + C.Bold + Manager.GetGame().GetType().GetName() : "     ", 
						"Visit " + C.cGreen + "theendlessweb.com" + ChatColor.RESET + " for News, Forums and Shop");
		
		//Lobby Name
		Manager.GetLobby().AddPlayerToScoreboards(player, null);

		//Lobby Spawn
		if (Manager.GetGame() == null || !Manager.GetGame().InProgress())
		{
			Manager.Clear(player);
			player.teleport(Manager.GetLobby().GetSpawn());
			return;
		}

		//Game Spawn
		if (Manager.GetGame().IsAlive(player))
		{
			Location loc = Manager.GetGame().GetLocationStore().remove(player.getName());
			if (loc != null && !loc.getWorld().getName().equalsIgnoreCase("world"))
			{
				player.teleport(loc);
			}			
			else
			{
				Manager.Clear(player);
				player.teleport(Manager.GetGame().GetTeam(player).GetSpawn());
			}
		} 
		else
		{
			Manager.Clear(player);
			Manager.addSpectator(player, true);
			UtilPlayer.message(player, F.main("Game", Manager.GetGame().GetName() + " is in progress, please wait for next game!"));
		}

		player.setScoreboard(Manager.GetGame().GetScoreboard().GetScoreboard());
	}

	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent event)
	{
		if (Manager.GetGame() == null || !Manager.GetGame().InProgress())
		{
			event.setRespawnLocation(Manager.GetLobby().GetSpawn());
			return;
		}

		Player player = event.getPlayer();

		if (Manager.GetGame().IsAlive(player))
		{
			event.setRespawnLocation(Manager.GetGame().GetTeam(player).GetSpawn());
		}
		else
		{
			Manager.addSpectator(player, true);

			event.setRespawnLocation(Manager.GetGame().GetSpectatorLocation());
		}
	}
	
	@EventHandler
	public void PlayerStateChange(PlayerStateChangeEvent event)
	{
		Recharge.Instance.useForce(event.GetPlayer(), "Return to Hub", 4000);
	}
	
	@EventHandler
	public void DisallowCreativeClick(InventoryClickEvent event)
	{
		if (Manager.GetGame() == null || !Manager.GetGame().InProgress() || Manager.GetGameHostManager().isEventServer() || Manager.GetGame().GetType() == GameType.Build)
			return;
		
		if ((event.getInventory().getType() == InventoryType.CREATIVE || event.getInventory().getType() == InventoryType.PLAYER) && !event.getWhoClicked().isOp())
		{
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void TeamInteract(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() == null)
			return;

		Player player = event.getPlayer();

		GameTeam team = Manager.GetLobby().GetClickedTeam(event.getRightClicked());

		if (team == null)
			return;

		//Observer
		if (Manager.IsObserver(player))
		{
			UtilPlayer.message(player, F.main("Game", "Spectators cannot partake in games."));
			return;
		}
		
		TeamClick(player, team);
	}

	@EventHandler
	public void TeamDamage(CustomDamageEvent event)
	{
		Player player = event.GetDamagerPlayer(false);
		if (player == null)		return;

		LivingEntity target = event.GetDamageeEntity();

		GameTeam team = Manager.GetLobby().GetClickedTeam(target);

		if (team == null)
			return;
		
		//Observer
		if (Manager.IsObserver(player))
		{
			UtilPlayer.message(player, F.main("Game", "Spectators cannot partake in games."));
			return;
		}
		
		TeamClick(player, team);
	}

	public void TeamClick(final Player player, final GameTeam team)
	{
		if (Manager.GetGame() == null)
			return;

		if (Manager.GetGame().GetState() != GameState.Recruit)
			return;

		if (!Manager.GetGame().HasTeam(team))
			return;

		AddTeamPreference(Manager.GetGame(), player, team);
	}

	public void AddTeamPreference(Game game, Player player, GameTeam team)
	{
		GameTeam past = game.GetTeamPreference(player);

		GameTeam current = game.GetTeam(player);
		if (current != null && current.equals(team))
		{
			game.RemoveTeamPreference(player);
			UtilPlayer.message(player, F.main("Team", "You are already on " + F.elem(team.GetFormattedName()) + "."));
			return;
		}

		if (past == null || !past.equals(team))
		{
			if (past != null)
			{
				game.RemoveTeamPreference(player);
			}	

			if (!game.GetTeamPreferences().containsKey(team))
				game.GetTeamPreferences().put(team, new ArrayList<Player>());

			game.GetTeamPreferences().get(team).add(player);
		}

		UtilPlayer.message(player, F.main("Team", "You are " + F.elem(game.GetTeamQueuePosition(player)) + " in queue for " + F.elem(team.GetFormattedName() + " Team") + "."));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void KitInteract(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() == null)
			return;

		Player player = event.getPlayer();

		

		Kit kit = Manager.GetLobby().GetClickedKit(event.getRightClicked());

		if (kit == null)
			return;
		
		//Observer
		if (Manager.IsObserver(player) || Manager.isSpectator(player)) 
		{
			UtilPlayer.message(player, F.main("Game", "Spectators cannot partake in games."));
			return;
		}

		KitClick(player, kit, event.getRightClicked());

		event.setCancelled(true);
	}

	@EventHandler
	public void KitDamage(CustomDamageEvent event)
	{
		if (Manager.GetGame() != null && Manager.GetGame().InProgress())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player player = event.GetDamagerPlayer(false);
		if (player == null)		return;

		

		LivingEntity target = event.GetDamageeEntity();

		Kit kit = Manager.GetLobby().GetClickedKit(target);

		if (kit == null)
			return;
		
		//Observer
		if (Manager.IsObserver(player) || Manager.isSpectator(player))
		{
			UtilPlayer.message(player, F.main("Game", "Spectators cannot partake in games."));
			return;
		}
		
		KitClick(player, kit, target);
	}

	public void KitClick(final Player player, final Kit kit, final Entity entity)
	{
		kit.DisplayDesc(player);

		if (Manager.GetGame() == null)
			return;

		if (!Manager.GetGame().HasKit(kit))
			return;

		Donor donor = Manager.GetDonation().Get(player.getName());

		if (kit.GetAvailability() == KitAvailability.Free || 									//Free
			
			Manager.hasKitsUnlocked(player) || 														//YouTube
			
			(kit.GetAvailability() == KitAvailability.Achievement && 							//Achievement
			Manager.GetAchievement().hasCategory(player, kit.getAchievementRequirement())) ||	
			
			donor.OwnsUnknownPackage(Manager.GetGame().GetName() + " " + kit.GetName()) ||		//Green
			
			Manager.GetClients().Get(player).GetRank().Has(Rank.MAPDEV) ||						//STAFF
			
			donor.OwnsUnknownPackage(Manager.GetServerConfig().ServerType + " ULTRA") ||		//Single Ultra (Old)
			
			Manager.GetServerConfig().Tournament)												//Tournament
		{
			Manager.GetGame().SetKit(player, kit, true);
		}
		else if (kit.GetAvailability() == KitAvailability.Gem && donor.GetBalance(CurrencyType.Gems) >= kit.GetCost())
		{
			Manager.GetShop().openPageForPlayer(player, new ConfirmationPage<ArcadeManager, ArcadeShop>(
					Manager, Manager.GetShop(), Manager.GetClients(), Manager.GetDonation(), new Runnable()
			{
				public void run()
				{
					if (player.isOnline())
					{
						Manager.GetGame().SetKit(player, kit, true);
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(entity.getEntityId(), ((CraftEntity) entity).getHandle().getDataWatcher(), true));
					}
				}
			}, null, new KitPackage(Manager.GetGame().GetName(), kit), CurrencyType.Gems, player));
		}
		else if (kit.GetAvailability() == KitAvailability.Achievement)
		{
			UtilPlayer.message(player, F.main("Kit", "You have not unlocked all " + F.elem(C.cPurple + Manager.GetGame().GetName() + " Achievements") + "."));
		}
		else
		{
			player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 0.5f);

			UtilPlayer.message(player, F.main("Kit", "You do not have enough " + F.elem(C.cGreen + "Gems") + "."));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void TeleportCommand(PlayerCommandPreprocessEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)
			return;
		
		Player player = event.getPlayer();
		
		if (Manager.GetClients().Get(player).GetRank().Has(Rank.MODERATOR))
			return;
		
		if (event.getMessage().toLowerCase().startsWith("/tp"))
		{
			UtilPlayer.message(player, F.main("Game", "Spectate Teleport changed to " + F.elem("/spec <name>") + "."));
			event.setCancelled(true);
			return;
		}
		
		if (!event.getMessage().toLowerCase().startsWith("/spec"))
			return;
		
		event.setCancelled(true);
		
		if (game.IsAlive(player) || !Manager.isSpectator(player))
		{
			return;
		}
		
		String[] tokens = event.getMessage().split(" ");
		
		if (tokens.length != 2)
		{
			UtilPlayer.message(player, F.main("Game", "Invalid Input. " + F.elem("/spec <Name>") + "."));
			return;
		}
		
		Player target = UtilPlayer.searchOnline(player, tokens[1], true);
		if (target != null)
		{
			UtilPlayer.message(player, F.main("Game", "You teleported to " + F.name(target.getName()) + "."));
			player.teleport(target);
		}
	}
}
