package nautilus.game.arcade.managers;

import java.util.HashMap;
import java.util.UUID;

import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.leaderboard.LeaderboardManager;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.stats.StatTracker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameStatManager implements Listener
{
	ArcadeManager Manager;
	
	private final HashMap<UUID, Long> _joinTimes = new HashMap<>();

	public GameStatManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}	

	@EventHandler
	public void StatEnableDisable(GameStateChangeEvent event) 
	{
		if (!Manager.IsRewardStats())
			return;
		
		if (event.GetState() != GameState.Live)
			return; 
		
		//int requirement = (int)((double)event.GetGame().Manager.GetPlayerFull() * 0.75d);
		int requirement = 2;
		
		event.GetGame().CanAddStats = (double)event.GetGame().GetPlayers(true).size() >= requirement;
		
		if (!event.GetGame().CanAddStats)
			event.GetGame().Announce(C.Bold + "Stats/Achievements Disabled. Requires " + requirement + " Players.", event.GetGame().PlaySoundGameStart);
	}
			
	@EventHandler
	public void StatRegister(GameStateChangeEvent event) 
	{
		if (!Manager.IsRewardStats())
			return;
		
		if (event.GetState() != GameState.Dead)
			return; 

		for (Player player : event.GetGame().GetStats().keySet())
		{
			for (String stat : event.GetGame().GetStats().get(player).keySet())
			{
				int value = event.GetGame().GetStats().get(player).get(stat);
				Manager.GetStatsManager().incrementStat(player, stat, value);
				
				// Leaderboard hook for logging appropriate stat events
				// Note: Rejects stat events that are not of the appropriate types.
				int gameId = event.GetGame().GetType().getGameId();
				LeaderboardManager.getInstance().attemptStatEvent(player, stat.split("\\.")[1], gameId, value);
			}
		}
	}
	
	@EventHandler
	public void StatGameRecord(GameStateChangeEvent event)
	{
		if (event.GetState() == GameState.End)
		{
			for (StatTracker tracker : event.GetGame().getStatTrackers())
				HandlerList.unregisterAll(tracker);

			if (event.GetGame().CanAddStats)
			{
				//Manager.saveBasicStats(event.GetGame());

				//if (Manager.IsTournamentServer())
				//	Manager.saveLeaderboardStats(event.GetGame());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playTimeStatJoin(PlayerJoinEvent event)
	{
		_joinTimes.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
	}

	@EventHandler
	public void playTimeStatQuit(PlayerQuitEvent event)
	{
		Long joinTime = _joinTimes.remove(event.getPlayer().getUniqueId());

		if (joinTime != null)
		{
			int timeInGame = (int) ((System.currentTimeMillis() - joinTime) / 1000);
			
			Manager.GetStatsManager().incrementStat(event.getPlayer(), "Global.TimeInGame", timeInGame);
		}
	}
	
	@EventHandler
	public void statBoostCommand(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().startsWith("/statboost ") && Manager.GetClients().Get(event.getPlayer()).GetRank().Has(Rank.OWNER))
		{
			event.setCancelled(true);
			
			String[] tokens = event.getMessage().split(" ");
			
			if (tokens.length < 2)
				return;
			
			Player player = UtilPlayer.searchOnline(event.getPlayer(), tokens[1], true);
			if (player == null)
				return;
			
			for (GameType type : GameType.values())
			{
				int wins = UtilMath.r(50);
				int loss = UtilMath.r(50);
				int play = wins+loss;
					
				Manager.GetStatsManager().incrementStat(player, type.GetName() + ".Wins", wins);
				Manager.GetStatsManager().incrementStat(player, type.GetName() + ".Losses", loss);
				Manager.GetStatsManager().incrementStat(player, type.GetName() + ".GamesPlayed", play);
			}
			
			event.getPlayer().sendMessage("Gave Stats: " + player.getName());
		}
	}
}
