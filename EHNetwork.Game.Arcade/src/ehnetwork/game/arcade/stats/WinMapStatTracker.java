package ehnetwork.game.arcade.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.dragonescape.DragonEscape;

public class WinMapStatTracker extends StatTracker<Game>
{
	public WinMapStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDragonEscapePlayerFinish(DragonEscape.PlayerFinishEvent event)
	{
		addStat(event.getPlayer(), "Win." + getGame().WorldData.MapName, 1, true, false);
	}
}
