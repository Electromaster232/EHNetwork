package nautilus.game.arcade.game;

import mineplex.core.common.util.*;
import mineplex.core.common.util.UtilTime.*;
import mineplex.core.updater.*;
import mineplex.core.updater.event.*;
import nautilus.game.arcade.*;
import nautilus.game.arcade.events.*;
import nautilus.game.arcade.game.GameTeam.*;
import nautilus.game.arcade.kit.*;
import nautilus.game.arcade.stats.TeamDeathsStatTracker;
import nautilus.game.arcade.stats.TeamKillsStatTracker;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;

public abstract class TeamGame extends Game
{
	protected ArrayList<GameTeam> _places = new ArrayList<GameTeam>();

	private NautHashMap<String, Long> _rejoinTime = new NautHashMap<String, Long>();
	protected NautHashMap<String, GameTeam> RejoinTeam = new NautHashMap<String, GameTeam>();
	protected NautHashMap<String, Kit> RejoinKit = new NautHashMap<String, Kit>();
	protected NautHashMap<String, Double> RejoinHealth = new NautHashMap<String, Double>();

	protected long RejoinTime = 120000;

	public TeamGame(ArcadeManager manager, GameType gameType, Kit[] kits, String[] gameDesc)
	{
		super(manager, gameType, kits, gameDesc);
	}

	@EventHandler
	public void EndStateChange(PlayerStateChangeEvent event)
	{
		GameTeam team = this.GetTeam(event.GetPlayer());
		if (team == null)
			return;

		if (event.GetState() == PlayerState.OUT)
			if (!team.IsTeamAlive())
				_places.add(0, team);

			else
				_places.remove(team);
	}

	public ArrayList<GameTeam> GetPlaces()
	{
		return _places;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerQuit(PlayerQuitEvent event)
	{
		if (!InProgress())
			return;

		Player player = event.getPlayer();

		GameTeam team = GetTeam(player);
		if (team == null) return;

		if (!team.IsAlive(player))
			return;

		team.RemovePlayer(player);

		if (player.isDead())
			return;

		if (player.getWorld().getName().equalsIgnoreCase("world"))
			return;

		if (!QuitOut)
		{
			//Store
			_rejoinTime.put(player.getName(), System.currentTimeMillis());
			RejoinTeam.put(player.getName(), team);

			if (GetKit(player) != null)
				RejoinKit.put(player.getName(), GetKit(player));

			RejoinHealth.put(player.getName(), player.getHealth());

			GetLocationStore().put(player.getName(), player.getLocation());

			//Announcement
			Announce(team.GetColor() + C.Bold + player.getName() + " has disconnected! " + UtilTime.convert(RejoinTime, 0, TimeUnit.MINUTES) + " minutes to rejoin.", false);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerLoginAllow(PlayerLoginEvent event)
	{
		if (!InProgress() || QuitOut)
			return;

		//Rejoined
		GameTeam team = RejoinTeam.remove(event.getPlayer().getName());
		if (team != null && _rejoinTime.remove(event.getPlayer().getName()) != null)
		{
			team.AddPlayer(event.getPlayer(), true);
			Announce(team.GetColor() + C.Bold + event.getPlayer().getName() + " has reconnected!", false);


			Kit kit = RejoinKit.remove(event.getPlayer().getName());
			if (kit != null)
				_playerKit.put(event.getPlayer(), kit);

//			final Player player = event.getPlayer();
//			Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
//			{
//				@Override
//				public void run() 
//				{
//					if (RejoinHealth.containsKey(player.getName()))
//					{
//						double health = RejoinHealth.remove(player.getName());
//						player.setHealth(health);
//						player.sendMessage("DEBUG: restored hp to " + health);
//					}
//				}
//			}, 20);
			
			return;
		}
	}
	
	//Do this on Join, not Login, otherwise player no get heal.
	@EventHandler
	public void playerRejoinGame(PlayerJoinEvent event)
	{
		if (!InProgress() || QuitOut)
			return;

		Player player = event.getPlayer();
		
		if (RejoinHealth.containsKey(player.getName()))
		{
			double health = RejoinHealth.remove(player.getName());
			player.setHealth(health);
		}
	}

	@EventHandler
	public void PlayerRejoinExpire(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC || QuitOut)
			return;

		Iterator<String> rejoinIterator = _rejoinTime.keySet().iterator();

		while (rejoinIterator.hasNext())
		{
			String name = rejoinIterator.next();

			if (!UtilTime.elapsed(_rejoinTime.get(name), RejoinTime))
				continue;

			rejoinIterator.remove();

			//Get Team (By Name)
			GameTeam team = RejoinTeam.remove(name);
			if (team != null)
				Announce(team.GetColor() + C.Bold + name + " did not reconnect in time!", false);

			RejoinKit.remove(name);
			RejoinHealth.remove(name);
		}
	}

	@EventHandler
	public void RejoinCommand(PlayerCommandPreprocessEvent event)
	{
		if (!QuitOut && event.getPlayer().isOp() && event.getMessage().startsWith("/allowrejoin"))
		{
			String[] toks = event.getMessage().split(" ");

			if (toks.length <= 1)
			{
				event.getPlayer().sendMessage("Missing Param!");
			}
			else
			{
				_rejoinTime.put(toks[1], System.currentTimeMillis());
				event.getPlayer().sendMessage("Allowed " + toks[1] + " to rejoin!");
			}

			event.setCancelled(true);
		}
	}

	public void EndCheck()
	{
		if (!IsLive())
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (!QuitOut)
		{
			//Offline Player Team
			for (GameTeam team : RejoinTeam.values())
				teamsAlive.add(team);
		}

		if (teamsAlive.size() <= 1)
		{
			//Announce
			if (teamsAlive.size() > 0)
				AnnounceEnd(teamsAlive.get(0));

			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
						AddGems(player, 10, "Winning Team", false, false);
				}

				for (Player player : team.GetPlayers(false))
				{
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
					if (this.getArcadeManager().GetBrandListener().playerUsingClient(player))
					{
						//UtilPlayer.message(player, "you were meant to get gems... DID YOU?????");
						AddGems(player, 75, "Using EHNetwork Client", false, true);
					}
				}
			}
			//End
			SetState(GameState.End);
		}
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event != null && event.getType() != UpdateType.FAST)
			return;

		Scoreboard.Reset();

		for (GameTeam team : this.GetTeamList())
		{
			//Display Individual Players
			if (this.GetPlayers(true).size() < 13)
			{
				if (!team.IsTeamAlive())
					continue;

				Scoreboard.WriteBlank();

				for (Player player : team.GetPlayers(true))
				{
					Scoreboard.Write(team.GetColor() + player.getName());
				}
			}

			//Display Players Alive
			else
			{
				Scoreboard.WriteBlank();

				Scoreboard.Write(team.GetColor() + team.GetName());
				Scoreboard.Write(team.GetPlayers(true).size() + "" + team.GetColor() + " Alive");
			}
		}

		Scoreboard.Draw();
	}

	@Override
	public List<Player> getWinners()
	{
		if (WinnerTeam == null)
			return null;

		return WinnerTeam.GetPlayers(false);
	}

	@Override
	public List<Player> getLosers()
	{
		if (WinnerTeam == null)
			return null;

		List<Player> players = new ArrayList<>();

		for (GameTeam team : GetTeamList())
		{
			if (team != WinnerTeam)
				players.addAll(team.GetPlayers(false));
		}

		return players;
	}
}
