package ehnetwork.game.arcade.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.draw.Draw;
import ehnetwork.game.arcade.game.games.draw.DrawGuessCorrectlyEvent;

public class PureLuckStatTracker extends StatTracker<Draw>
{
	public PureLuckStatTracker(Draw game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDrawGuessCorrectly(DrawGuessCorrectlyEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (System.currentTimeMillis() - event.getDrawRound().Time < 8000)
			addStat(event.getPlayer(), "PureLuck", 1, true, false);
	}
}
