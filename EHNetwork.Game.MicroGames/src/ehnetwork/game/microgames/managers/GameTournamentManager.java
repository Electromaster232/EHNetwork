package ehnetwork.game.microgames.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.game.GameTeam;

public class GameTournamentManager implements Listener
{
	MicroGamesManager Manager;

	private HashMap<String, String> _tournamentTeam = new HashMap<String, String>();
	
	private HashMap<String, Integer> _tournamentPoints = new HashMap<String, Integer>();
	
	public GameTournamentManager(MicroGamesManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}
	@EventHandler
	public void TeamTournamentAutoJoin(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Game game = Manager.GetGame();
		if (game == null)	return;
		
		if (game.GetState() != GameState.Recruit)
			return;
		
		for (Player player : UtilServer.getPlayers())
			rejoinTournamentTeam(player);
	}
	
	public void setTournamentTeam(Player player, GameTeam team)
	{
		if (!Manager.IsTeamRejoin())
			return;

		_tournamentTeam.put(player.getName(), team.GetName());
	}

	public void rejoinTournamentTeam(Player player)
	{
		if (Manager.GetGame() == null)
			return;

		if (!_tournamentTeam.containsKey(player.getName()))
			return;

		GameTeam curPref = Manager.GetGame().GetTeamPreference(player);
		if (curPref != null)
			return;

		GameTeam curTeam = Manager.GetGame().GetTeam(player);
		if (curTeam != null)
			return;

		String teamName = _tournamentTeam.get(player.getName());

		for (GameTeam team : Manager.GetGame().GetTeamList())
		{
			if (team.GetName().equalsIgnoreCase(teamName))
			{
				Manager.GetGamePlayerManager().AddTeamPreference(Manager.GetGame(), player, team);
				break;
			}
		}
	}
	
	@EventHandler
	public void pointsReward(GameStateChangeEvent event)
	{
		if (!Manager.IsTournamentPoints())
			return;
		
		if (event.GetState() != GameState.End)
			return;
		
		ArrayList<Player> places = event.GetGame().GetTeamList().get(0).GetPlacements(true);
		
		for (int i=0 ; i<places.size() ; i++)
		{
			Player player = places.get(i);
			int points = places.size() - i;
			
			addTournamentPoints(player, points);
			
			UtilPlayer.message(player, 
					F.main("Tournament", "You received " + F.elem(points + " Points") + " for placing " + F.elem((i+1)+"/"+places.size()) + "."));
		}
	}
	
	@EventHandler
	public void pointsScoreboard(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!Manager.IsTournamentPoints())
			return;
		
		for (Scoreboard scoreboard : Manager.GetLobby().GetScoreboards())
		{
			Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);
			
			if (objective == null)
			{
				objective = scoreboard.registerNewObjective("Points", "dummy");
				objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			}
			
			for (Player player : UtilServer.getPlayers())
				objective.getScore(player.getName()).setScore(getTournamentPoints(player.getName()));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void pointsDisplay(GameStateChangeEvent event)
	{
		if (!Manager.IsTournamentPoints())
			return;
		
		if (event.GetState() != GameState.Dead)
			return;

		ArrayList<String> ranks = new ArrayList<String>();
		
		while (ranks.size() < _tournamentPoints.size())
		{
			int bestPoints = 0;
			String bestPlayer = null;
			
			for (String cur : _tournamentPoints.keySet())
			{
				if (ranks.contains(cur))
					continue;
				
				int points = _tournamentPoints.get(cur);
				
				if (points == 0)
					continue;
				
				if (bestPlayer == null || points < bestPoints)
				{
					bestPlayer = cur;
					bestPoints = points;
				}
			}
			
			if (bestPlayer == null)
				break;
			
			ranks.add(bestPlayer);
		}
		
		Bukkit.broadcastMessage(C.cBlue + C.Strike + C.Bold + "--------------------------------------------");
		
		for (int i=0 ; i<ranks.size() ; i++)
		{
			Bukkit.broadcastMessage(C.cGold + getTournamentPoints(ranks.get(i)) + " Points" + "   " + C.cWhite + ranks.get(i));
		}
		
		Bukkit.broadcastMessage(C.cBlue + C.Strike + C.Bold + "--------------------------------------------");
	}
	
	public int getTournamentPoints(String player)
	{
		if (!_tournamentPoints.containsKey(player))
			_tournamentPoints.put(player, 0);
		
		return _tournamentPoints.get(player);
	}
	
	public void addTournamentPoints(Player player, int points)
	{
		_tournamentPoints.put(player.getName(), points + getTournamentPoints(player.getName()));
	}
	
	@EventHandler
	public void clean(PlayerQuitEvent event)
	{
		_tournamentTeam.remove(event.getPlayer().getName());
	}
}
