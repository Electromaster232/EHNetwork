package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.skywars.events.TNTPickupEvent;

public class SkywarsTNTStatTracker extends StatTracker<Game>
{

	public SkywarsTNTStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onTNTPickup(TNTPickupEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getWho(), "BombPickups", 1, false, false);
	}
	
}
