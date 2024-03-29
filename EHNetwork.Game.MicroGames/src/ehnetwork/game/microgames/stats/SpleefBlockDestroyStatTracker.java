package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.spleef.SpleefDestroyBlockEvent;

public class SpleefBlockDestroyStatTracker extends StatTracker<Game>
{
	public SpleefBlockDestroyStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onSpleefDestroyBlock(SpleefDestroyBlockEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getPlayer(), "SpleefBlocks", 1, false, false);
	}
}
