package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class KillsStatTracker extends StatTracker<Game>
{
	public KillsStatTracker(Game game)
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

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)
			return;

		addStat(player, "Kills", 1, false, false);

//		if (getGame().GetKit(player) != null)
//			addStat(player, getGame().GetKit(player).GetName() + " Kills", 1, false, false);
	}
}
