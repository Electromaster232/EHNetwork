package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;

public class GamesPlayedStatTracker extends StatTracker<Game>
{
	public GamesPlayedStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.Live)
		{
			for (Player player : getGame().GetPlayers(true))
				addStat(player, "GamesPlayed", 1, false, true);
		}
	}
}
