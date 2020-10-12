package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class TheLongestShotStatTracker extends StatTracker<Game>
{
	public TheLongestShotStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (!getGame().IsLive())
			return;

		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (killer == null)
			return;

		if (event.GetLog().GetPlayer() == null)
			return;

		if (!event.GetLog().GetPlayer().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetPlayer().GetName());
		
		if (player == null || !player.isOnline())
			return;

		if (event.GetLog().GetLastDamager().GetReason() != null && event.GetLog().GetLastDamager().GetReason().toLowerCase().contains("longshot"))
		{
			if (killer.getLocation().distance(player.getLocation()) >= 64)
			{
				addStat(killer, "TheLongestShot", 1, false, false);
			}
		}
	}
}
