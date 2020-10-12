package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.kit.perks.PerkBomber;

public class TntMinerStatTracker extends StatTracker<Game>
{
	public TntMinerStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityExplode(PerkBomber.BomberExplodeDiamondBlock event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getPlayer(), "FortuneBomber", 1, false, false);
	}
}
