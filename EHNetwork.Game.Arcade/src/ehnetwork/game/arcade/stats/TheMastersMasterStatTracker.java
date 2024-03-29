package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.sneakyassassins.SneakyAssassins;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class TheMastersMasterStatTracker extends StatTracker<SneakyAssassins>
{
	public TheMastersMasterStatTracker(SneakyAssassins game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
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

		if (getGame().getPowerUpManager().getPowerUpCount(killer) == 0 && getGame().getPowerUpManager().getPowerUpCount(player) >= 4)
			addStat(killer, "TheMastersMaster", 1, true, false);
	}
}
