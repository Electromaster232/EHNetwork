package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.kit.perks.PerkRevealer;

public class RevealStatTracker extends StatTracker<Game>
{
	private final String _statName;

	public RevealStatTracker(Game game, String statName)
	{
		super(game);

		_statName = statName;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerReveal(PerkRevealer.PlayerRevealEvent event)
	{
		if (event.getRevealer() instanceof Player)
			addStat((Player) event.getRevealer(), getStatName(), 1, false, false);
	}

	public String getStatName()
	{
		return _statName;
	}
}
