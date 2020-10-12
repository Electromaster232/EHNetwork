package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.TeamGame;

public class WinWithoutLosingTeammateStatTracker extends StatTracker<TeamGame>
{
	private final String _stat;

	public WinWithoutLosingTeammateStatTracker(TeamGame game, String stat)
	{
		super(game);
		_stat = stat;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			GameTeam winner = getGame().WinnerTeam;

			if (winner == null)
				return;
			
			if (winner.GetPlayers(false).size() < 4)
				return;
			
			if (winner.GetPlayers(true).size() == winner.GetPlayers(false).size())
			{
				for (Player player : winner.GetPlayers(true))
					addStat(player, getStat(), 1, true, false);
			}
		}
	}

	public String getStat()
	{
		return _stat;
	}
}
