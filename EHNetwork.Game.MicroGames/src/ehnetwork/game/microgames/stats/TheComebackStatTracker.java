package ehnetwork.game.microgames.stats;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.games.tug.turfforts.TurfForts;

public class TheComebackStatTracker extends StatTracker<TurfForts>
{
	private final Set<GameTeam> _hasWentFiveOrBelow = new HashSet<>();

	public TheComebackStatTracker(TurfForts game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onUpdate(UpdateEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (event.getType() == UpdateType.TICK)
		{
			for (GameTeam team : getGame().GetTeamList())
			{
				if (getGame().getLines(team) <= 5)
					_hasWentFiveOrBelow.add(team);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			if (_hasWentFiveOrBelow.contains(getGame().WinnerTeam))
			{
				if (getGame().getWinners() != null)
				{
					for (Player player : getGame().getWinners())
						addStat(player, "TheComeback", 1, true, false);
				}
			}
		}
	}
}
