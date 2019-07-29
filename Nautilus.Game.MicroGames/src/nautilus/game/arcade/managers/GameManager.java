package nautilus.game.arcade.managers;

import java.util.ArrayList;
import java.util.Iterator;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextBottom;
import mineplex.core.common.util.UtilTextTop;
import mineplex.core.common.util.UtilTime;
import mineplex.core.gadget.gadgets.MorphWither;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.gadget.types.GadgetType;
import mineplex.minecraft.game.core.condition.Condition.ConditionType;
import mineplex.core.mount.Mount;
import mineplex.core.mount.types.MountDragon;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.RestartServerEvent;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GamePrepareCountdownCommence;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerPrepareTeleportEvent;
import nautilus.game.arcade.events.PlayerStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.games.uhc.UHC;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GameManager implements Listener
{
	ArcadeManager Manager;
	
	private int _colorId = 0;

	public GameManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}
	
	@EventHandler
	public void DisplayIP(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		if (Manager.GetGame() != null && !Manager.GetGame().InProgress())
		{
			ChatColor col = ChatColor.RED;
			if (_colorId == 1)	col = ChatColor.YELLOW;
			else if (_colorId == 2)	col = ChatColor.GREEN;
			else if (_colorId == 3)	col = ChatColor.AQUA;
			_colorId = (_colorId+1)%4;
			
			String text = col + C.Bold + "   GUAC.DJELECTRO.ME   ";
			
			double health = 1;
			if (Manager.GetGame().GetState() == GameState.Recruit)
			{
				if (Manager.GetGame().GetCountdown() >= 0 && Manager.GetGame().GetCountdown() <= 10)
					health = 1 - Manager.GetGame().GetCountdown() / 10d;
			}
			 
			//Display IP
			UtilTextTop.displayProgress(text, health, UtilServer.getPlayers());

			for (Creature pet : Manager.getCosmeticManager().getPetManager().getPets())
	        {
	            if (pet instanceof Wither)
	            {
	                pet.setCustomName(text);
	                pet.setHealth(Math.max(0.1, 300 * health));
	            }
	        }
			
			//Name Dragons Appropriately
			for (Mount mount : Manager.getCosmeticManager().getMountManager().getMounts())
			{
				if (mount instanceof MountDragon)
				{
					((MountDragon) mount).SetName(text);
					((MountDragon) mount).setHealthPercent(health);
				}
			}
			
			for (Gadget gadget : Manager.getCosmeticManager().getGadgetManager().getGadgets(GadgetType.Morph))
			{
				if (gadget instanceof MorphWither)
				{
					((MorphWither)gadget).setWitherData(text, health);
				}
			}
		}
	}
	
	@EventHandler
	public void DisplayPrepareTime(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (Manager.GetGame() == null || Manager.GetGame().GetState() != GameState.Prepare)
			return;

		Game game = Manager.GetGame();

		double percentage = (double) (System.currentTimeMillis() - game.GetStateTime()) / game.PrepareTime;
		
		for (Player player : UtilServer.getPlayers())
			UtilTextBottom.displayProgress("Game Start", percentage, 
					UtilTime.MakeStr(Math.max(0, game.PrepareTime - (System.currentTimeMillis() - game.GetStateTime()))), player);
	}

	@EventHandler
	public void StateUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.GetState() == GameState.Loading)
		{
			if (UtilTime.elapsed(game.GetStateTime(), 30000))
			{
				System.out.println("Game Load Expired.");
				game.SetState(GameState.Dead);
			}
		} 
		else if (game.GetState() == GameState.Recruit)
		{
			//Stop Countdown!
			if (game.GetCountdown() != -1 && 
					UtilServer.getPlayers().length < Manager.GetPlayerMin() && 
					!game.GetCountdownForce())
			{
				game.SetCountdown(-1);
				Manager.GetLobby().DisplayWaiting();
			}
	
			if(UtilServer.getPlayers().length >= 2){
				if(Manager.GetRotationPause()){
					return;
				}
				TeamDefaultJoin(game);
				game.SetState(GameState.Prepare);
			}
		}
		else if (game.GetState() == GameState.Prepare)
		{
			if (game.CanStartPrepareCountdown())
			{
				if (UtilTime.elapsed(game.GetStateTime(), game.PrepareTime))
				{
					int players = game.GetPlayers(true).size();
					
					if (players < 2 || game.PlaySoundGameStart)
					{
						for (Player player : UtilServer.getPlayers())
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 2f, 2f);
					}

					if (players < 2) 
					{
						game.Announce(C.cWhite + C.Bold + game.GetName() + " ended, not enough players!");
						game.SetState(GameState.Dead);
					}
					else
					{
						game.SetState(GameState.Live);
					}
				}
				else
				{
					for (Player player : UtilServer.getPlayers())
						player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
				}
			}
		}
		else if (game.GetState() == GameState.Live)
		{
			if (game.GameTimeout != -1 && UtilTime.elapsed(game.GetStateTime(), game.GameTimeout) && Manager.IsGameTimeout())
			{
				game.HandleTimeout();
			}
		}
		else if (game.GetState() == GameState.End)
		{
			if (UtilTime.elapsed(game.GetStateTime(), 0))
			{
				game.SetState(GameState.Dead);
			}
		}
	}

	public void StateCountdown(Game game, int timer, boolean force)
	{
		if (game instanceof UHC && !((UHC)game).isMapLoaded())
			return;

		if (Manager.GetGameHostManager().isPrivateServer() && Manager.GetGameHostManager().isVoteInProgress())
			return;

		//Disabling Cosmetics
		if (game.GetCountdown() <= 5 && game.GetCountdown() >= 0 && game.GadgetsDisabled)
		{
			if (Manager.getCosmeticManager().isShowingInterface())
			{
				Manager.getCosmeticManager().setActive(false);
				Manager.getCosmeticManager().disableItemsForGame();
			}
		}

		if (force)
			game.SetCountdownForce(true);
		
		//Team Preference
		TeamPreferenceJoin(game);

		//Team Swap
		TeamPreferenceSwap(game);

		//Team Default
		TeamDefaultJoin(game);

		//Team Inform STILL Queued
		if (game.GetCountdown() == -1)
		{
			game.InformQueuePositions();
			//game.AnnounceGame();
		}
		
		//Initialise Countdown
		if (force)
			game.SetCountdownForce(true);

		//Start  Timer
		if (game.GetCountdown() == -1)
			game.SetCountdown(timer + 1);

		//Decrease Timer
		if (game.GetCountdown() > timer + 1 && timer != -1)
			game.SetCountdown(timer + 1);

		//Countdown--
		if (game.GetCountdown() > 0)
			game.SetCountdown(game.GetCountdown() - 1);

		//Inform Countdown
		if (game.GetCountdown() > 0)		
		{
			Manager.GetLobby().WriteGameLine("starting in " + game.GetCountdown() + "...", 3, 159, (byte)13);
		}
		else					
		{
			Manager.GetLobby().WriteGameLine("game in progress", 3, 159, (byte)13);
		}

		if (game.GetCountdown() > 0 && game.GetCountdown() <= 10)
			for (Player player : UtilServer.getPlayers())
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);

		//Countdown Ended
		if (game.GetCountdown() == 0)
			game.SetState(GameState.Prepare);
	}
	
	@EventHandler
	public void restartServerCheck(RestartServerEvent event)
	{
		if (Manager.GetGame() != null && Manager.GetGame().GetState() != GameState.Recruit)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void KitRegister(GameStateChangeEvent event) 
	{
		if (event.GetState() != event.GetGame().KitRegisterState)
			return;

		event.GetGame().RegisterKits();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void KitDeregister(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Dead)
			return;

		event.GetGame().DeregisterKits();
		event.GetGame().deRegisterStats();
	}
	
	
	
	@EventHandler
	public void ScoreboardTitle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;
		
		game.GetScoreboard().UpdateTitle();
	}

	@EventHandler(priority = EventPriority.LOWEST)	//BEFORE PARSE DATA
	public void TeamGeneration(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;

		Game game = event.GetGame();
		int count = 1;
		
		for (String team : game.WorldData.SpawnLocs.keySet())
		{
			ChatColor color;

			if (team.equalsIgnoreCase("RED"))			color = ChatColor.RED;
			else if (team.equalsIgnoreCase("YELLOW"))	color = ChatColor.YELLOW;
			else if (team.equalsIgnoreCase("GREEN"))	color = ChatColor.GREEN;
			else if (team.equalsIgnoreCase("BLUE"))		color = ChatColor.AQUA;
			else
			{
				color = ChatColor.DARK_GREEN;

				if (game.GetTeamList().size()%14 == 0) 		if (game.WorldData.SpawnLocs.size() > 1)		color = ChatColor.RED;
				if (game.GetTeamList().size()%14 == 1) 		color = ChatColor.YELLOW;
				if (game.GetTeamList().size()%14 == 2) 		color = ChatColor.GREEN;
				if (game.GetTeamList().size()%14 == 3) 		color = ChatColor.AQUA;
				if (game.GetTeamList().size()%14 == 4) 		color = ChatColor.GOLD;
				if (game.GetTeamList().size()%14 == 5) 		color = ChatColor.LIGHT_PURPLE;
				if (game.GetTeamList().size()%14 == 6) 		color = ChatColor.DARK_BLUE;
				if (game.GetTeamList().size()%14 == 7) 		color = ChatColor.WHITE;
				if (game.GetTeamList().size()%14 == 8) 		color = ChatColor.BLUE;
				if (game.GetTeamList().size()%14 == 9) 		color = ChatColor.DARK_GREEN;
				if (game.GetTeamList().size()%14 == 10) 	color = ChatColor.DARK_PURPLE;
				if (game.GetTeamList().size()%14 == 11) 	color = ChatColor.DARK_RED;
				if (game.GetTeamList().size()%14 == 12) 	color = ChatColor.DARK_AQUA;
			}
			
			//Random Names
			String teamName = team;
			if (game.WorldData.SpawnLocs.size() > 12)
			{
				teamName = "" + count;
				count++;
			}

			GameTeam newTeam = new GameTeam(game, teamName, color, game.WorldData.SpawnLocs.get(team));
			game.AddTeam(newTeam);
		}

		//Restrict Kits
		game.RestrictKits();

		//Parse Data
		game.ParseData();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void TeamScoreboardCreation(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Recruit)
			return;

		event.GetGame().GetScoreboard().CreateTeams();
	}

	public void TeamPreferenceJoin(Game game)
	{
		//Preferred Team No Longer Full
		for (GameTeam team : game.GetTeamPreferences().keySet())
		{	
			Iterator<Player> queueIterator = game.GetTeamPreferences().get(team).iterator();

			while (queueIterator.hasNext())
			{
				Player player = queueIterator.next();

				if (!game.CanJoinTeam(team))
				{
					break;
				}
									
				queueIterator.remove();

				if (!game.IsPlaying(player))
				{
					PlayerAdd(game, player, team);
				}
				else
				{
					game.SetPlayerTeam(player, team, true);
				}
			}
		}
	}

	public void TeamPreferenceSwap(Game game)
	{
		//Preferred Team No Longer Full
		for (GameTeam team : game.GetTeamPreferences().keySet())
		{	
			Iterator<Player> queueIterator = game.GetTeamPreferences().get(team).iterator();

			while (queueIterator.hasNext())
			{
				Player player = queueIterator.next();

				GameTeam currentTeam = game.GetTeam(player);

				//Not on team, cannot swap
				if (currentTeam == null)
					continue;

				// Other without concurrent (order doesn't matter as first case will fire
				if (team == currentTeam)
				{
					queueIterator.remove();
					continue;
				}

				for (Player other : team.GetPlayers(false))
				{
					if (other.equals(player))
						continue;

					GameTeam otherPref = game.GetTeamPreference(other);
					if (otherPref == null)
						continue;

					if (otherPref.equals(currentTeam))
					{
						UtilPlayer.message(player, F.main("Team", "You swapped team with " + F.elem(team.GetColor() + other.getName()) + "."));
						UtilPlayer.message(other, F.main("Team", "You swapped team with " + F.elem(currentTeam.GetColor() + player.getName()) + "."));

						//Player Swap
						queueIterator.remove();
						game.SetPlayerTeam(player, team, true);

						//Other Swap
						game.SetPlayerTeam(other, currentTeam, true);
						
						break;
					}
				}		
			}
		}
	}

	public void TeamDefaultJoin(Game game) 
	{
		//Team Default
		for (Player player : UtilServer.getPlayers())
		{
			if (player.isDead())
			{
				player.sendMessage(F.main("Afk Monitor", "You are being sent to the Lobby for being AFK."));
				Manager.GetPortal().sendPlayerToServer(player, "Lobby");
			}
			else if (Manager.IsObserver(player))
			{
				
			}
			else if (!game.IsPlaying(player) && Manager.IsTeamAutoJoin())
			{
				PlayerAdd(game, player, null);
			}
		}
	}

	@EventHandler
	public void TeamQueueSizeUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		for (GameTeam team : game.GetTeamList())
		{
			int amount = 0;
			if (game.GetTeamPreferences().containsKey(team))
			{
				amount = game.GetTeamPreferences().get(team).size();
			}

			if (team.GetTeamEntity() == null)
				continue;
			
			if (game.GetCountdown() == -1)
			{
				team.GetTeamEntity().setCustomName(team.GetFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " Queued");
			}
			else
			{
				team.GetTeamEntity().setCustomName(team.GetPlayers(false).size() + " Players  " + team.GetFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " Queued");
			}
		}
	}
	
	public boolean PlayerAdd(Game game, Player player, GameTeam team)
	{
		if (team == null)
			team = game.ChooseTeam(player);

		if (team == null)
			return false;

		game.SetPlayerTeam(player, team, true);

		return true;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PlayerPrepare(GameStateChangeEvent event)
	{
		final Game game = event.GetGame();

		if (event.GetState() != GameState.Prepare)
			return;

		final ArrayList<Player> players = game.GetPlayers(true);
		
		//Prepare Players
		for (int i=0 ; i<players.size() ; i++)
		{
			final Player player = players.get(i);
			
			final GameTeam team = game.GetTeam(player);
			
			UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					//Teleport
					team.SpawnTeleport(player);

					Manager.Clear(player);
					UtilInv.Clear(player);

					game.ValidateKit(player, game.GetTeam(player));

					if (game.GetKit(player) != null)
						game.GetKit(player).ApplyKit(player);
					
					//Event
					PlayerPrepareTeleportEvent playerStateEvent = new PlayerPrepareTeleportEvent(game, player);
					UtilServer.getServer().getPluginManager().callEvent(playerStateEvent);			
				}
			}, i * game.TickPerTeleport);
		}
		
		//Announce Game after every player is TP'd in
		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				game.AnnounceGame();
				game.StartPrepareCountdown();
				
				//Event
				GamePrepareCountdownCommence event = new GamePrepareCountdownCommence(game);
				UtilServer.getServer().getPluginManager().callEvent(event);			
			}
		}, players.size() * game.TickPerTeleport);
		
		//Spectators Move
		for (Player player : UtilServer.getPlayers())
		{
			if (Manager.GetGame().IsAlive(player))
				continue;
			
			Manager.addSpectator(player, true);
		}
	}

	@EventHandler
	public void PlayerTeleportOut(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Dead)
			return;
		
		final Player[] players = UtilServer.getPlayers();
		
		//Prepare Players
		for (int i=0 ; i<players.length ; i++)
		{
			final Player player = players[i];
			
			UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					Manager.Clear(player);
					UtilInv.Clear(player);
					
					Manager.GetCondition().EndCondition(player, ConditionType.CLOAK, "Spectator");
					
					player.eject();
					player.leaveVehicle();
					player.teleport(Manager.GetLobby().GetSpawn());		
				}
			}, i);
		}
	}
	
	@EventHandler
	public void disguiseClean(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Dead)
			return;

		Manager.GetDisguise().clearDisguises();
	}

 
	@EventHandler
	public void WorldFireworksUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		if (game.GetState() != GameState.End)
			return;

		Color color = Color.GREEN;

		if (game.WinnerTeam != null)
		{
			if (game.WinnerTeam.GetColor() == ChatColor.RED)				color = Color.RED;
			else if (game.WinnerTeam.GetColor() == ChatColor.AQUA)			color = Color.BLUE;
			else if (game.WinnerTeam.GetColor() == ChatColor.YELLOW)		color = Color.YELLOW;
			else															color = Color.LIME;
		}
		
		Location loc = game.GetSpectatorLocation().clone().add(Math.random()*160-80, 10 + Math.random()*20, Math.random()*160-80);

		//UtilFirework.playFirework(loc, Type.BALL_LARGE, color, false, false);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void EndUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		game.EndCheck();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void EndStateChange(PlayerStateChangeEvent event)
	{
		event.GetGame().EndCheck();
	}
}
