package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DamageDealtStatTracker extends StatTracker<Game>
{
	public DamageDealtStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCustomDamage(CustomDamageEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)
			return;

		addStat(damager, "Damage Dealt", (int) Math.round(event.GetDamage()), false, false);
		
//		if (getGame().GetKit(damager) != null)
//			addStat(damager, getGame().GetKit(damager).GetName() + " Damage Dealt", (int) Math.round(event.GetDamage()), false, false);
	}
}
