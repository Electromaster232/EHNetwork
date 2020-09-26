package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.minestrike.MineStrike;

public class HeadShotStatTracker extends StatTracker<Game>
{
	private final String _statName;

	public HeadShotStatTracker(Game game, String statName)
	{
		super(game);

		_statName = statName;
	}

	public String getStatName()
	{
		return _statName;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerHeadshot(MineStrike.PlayerHeadshotEvent event)
	{
		addStat(event.getShooter(), "Headshot", 1, false, false);
	}
}
