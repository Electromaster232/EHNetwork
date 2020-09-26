package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DamageTakenStatTracker extends StatTracker<Game>
{
	public DamageTakenStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCustomDamage(CustomDamageEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)
			return;
		
		addStat(damagee, "Damage Taken", (int) Math.round(event.GetDamage()), false, false);
		if (event.GetDamagerPlayer(true) != null)
			addStat(damagee, "Damage Taken PvP ", (int) Math.round(event.GetDamage()), false, false);
		
//		if (getGame().GetKit(damagee) != null)
//		{
//			addStat(damagee, getGame().GetKit(damagee).GetName() + " Damage Taken", (int) Math.round(event.GetDamage()), false, false);
//			
//			if (event.GetDamagerPlayer(true) != null)
//				addStat(damagee, getGame().GetKit(damagee).GetName() + " Damage Taken PvP ", (int) Math.round(event.GetDamage()), false, false);
//		}
	}
}
