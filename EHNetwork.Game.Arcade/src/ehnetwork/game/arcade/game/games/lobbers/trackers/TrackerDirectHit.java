package ehnetwork.game.arcade.game.games.lobbers.trackers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.stats.StatTracker;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class TrackerDirectHit extends StatTracker<Game>
{
	public TrackerDirectHit(Game game)
	{
		super(game);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKillDirectHit(CustomDamageEvent event)
	{
		if (!getGame().IsLive())
			return;
		
		if (event.GetDamagerPlayer(true) == null)
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		
		if (!getGame().IsAlive(damager))
			return;
			
		if (event.GetReason() == null)
			return;
		
		if (event.GetReason().toLowerCase().contains("direct hit"))
			addStat(damager, "Direct Hit", 1, false, false);
	}
}
