package ehnetwork.game.arcade.game.games.lobbers.trackers;

import org.bukkit.event.EventHandler;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.lobbers.events.TNTThrowEvent;
import ehnetwork.game.arcade.stats.StatTracker;

public class TrackerTNTThrown extends StatTracker<Game>
{
	
	public TrackerTNTThrown(Game game)
	{
		super(game);
	}
	
	@EventHandler
	public void onThrow(TNTThrowEvent event)
	{
		if (!getGame().IsLive())
			return;
	
		addStat(event.getPlayer(), "Thrown", 1, false, false);
	}
}
