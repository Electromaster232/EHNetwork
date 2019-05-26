package nautilus.game.arcade.stats;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import mineplex.core.common.util.NautHashMap;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.GemData;

public class ExperienceStatTracker extends StatTracker<Game>
{
	private long _startTime = -1;
	
	private NautHashMap<String, Long> _playerQuitTime = new NautHashMap<String, Long>();
	
	public ExperienceStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler
	public void onGameStart(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.Live)
			_startTime = System.currentTimeMillis();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		_playerQuitTime.put(event.getPlayer().getName(), System.currentTimeMillis());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameEnd(GameStateChangeEvent event)
	{
		if (!event.GetGame().Manager.IsRewardStats() || !event.GetGame().Manager.IsRewardAchievements())
			return;
		
		if (_startTime == -1)
			return;
		
		if (event.GetState() == Game.GameState.End)
		{
			List<Player> winners = getGame().getWinners();

			if (winners != null)
			{
				for (Player player : event.GetGame().GetPlayers(false))
				{
					//Tally Gems
					double gemExp = 0;
					for (String reason : event.GetGame().GetGems(player).keySet())
					{
						if (reason.toLowerCase().contains("participation"))
							continue;
						
						GemData gem = event.GetGame().GetGems(player).get(reason);
						
						gemExp += (int)gem.Gems;
					}
					gemExp = Math.min(gemExp, 250) * 6;
					
					//Game Time = 1 Exp per 3 Seconds
					long time = System.currentTimeMillis();
					
					//Exp Until They Quit
					if (!player.isOnline())
					{
						if (_playerQuitTime.containsKey(player.getName()))
						{
							time = _playerQuitTime.get(player.getName());
						}
						else
						{
							time = _startTime;
						}
					}						
					
					double timeExp = (time - _startTime)/1500d;
					
					//Mult
					double mult = 1;
					if (winners.contains(player))
						mult = 1.5;
					
					//Exp 
					int expGained = (int)((timeExp + gemExp)*mult); 
					
					//Record Global and per Game
					addStat(player, "ExpEarned", expGained, false, true);
					addStat(player, "ExpEarned", expGained, false, false);
				}
			}
			
			_startTime = -1;
		}
	}
}
