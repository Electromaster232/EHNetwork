package ehnetwork.game.arcade.stats;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;

public class WinAsTeamStatTracker extends StatTracker<TeamGame>
{
	private final GameTeam _team;
	private final String _stat;

	public WinAsTeamStatTracker(TeamGame game, GameTeam team, String stat)
	{
		super(game);

		_team = team;
		_stat = stat;
	}

	public GameTeam getTeam()
	{
		return _team;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			if (getGame().WinnerTeam == getTeam())
			{
				List<Player> winners = getGame().getWinners();

				if (winners != null)
				{
					for (Player winner : winners)
						addStat(winner, _stat, 1, false, false);
				}
			}
		}
	}
}
