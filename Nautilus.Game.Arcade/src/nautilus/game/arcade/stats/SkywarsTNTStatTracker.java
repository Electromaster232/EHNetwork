package nautilus.game.arcade.stats;

import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.games.skywars.events.TNTPickupEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

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
