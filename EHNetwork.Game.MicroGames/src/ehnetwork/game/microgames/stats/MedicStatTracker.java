package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.paintball.Paintball;

public class MedicStatTracker extends StatTracker<Game>
{
	public MedicStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(Paintball.ReviveEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getPlayer(), "Medic", 1, false, false);
	}
}
