package ehnetwork.game.microgames.game.games.lobbers.trackers;

import org.bukkit.event.EventHandler;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.lobbers.events.TNTThrowEvent;
import ehnetwork.game.microgames.stats.StatTracker;

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
