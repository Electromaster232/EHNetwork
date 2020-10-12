package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.SeismicSlam;

public class SeismicSlamStatTracker extends StatTracker<Game>
{
	public SeismicSlamStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLongshotHit(SeismicSlam.SeismicSlamEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (event.getTargets().size() >= 5)
			addStat(event.getPlayer(), "Earthquake", 1, true, false);
	}
}
