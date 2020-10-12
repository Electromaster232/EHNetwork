package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.games.bridge.Bridge;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class BridgesSniperStatTracker extends StatTracker<Bridge>
{
	public BridgesSniperStatTracker(Bridge game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (!getGame().IsLive())
			return;

		if (getGame().isBridgesDown())
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
		
		if (player == null)
			return;

		if (player == killer)
			return;
		
		if (event.GetLog().GetLastDamager().GetLastDamageSource().contains("Archery"))
		{
			addStat(killer, "Sniper", 1, true, false);
		}
	}
}
