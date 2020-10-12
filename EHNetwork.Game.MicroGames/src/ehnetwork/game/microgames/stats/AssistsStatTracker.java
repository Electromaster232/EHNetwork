package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.minecraft.game.core.combat.CombatComponent;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class AssistsStatTracker extends StatTracker<Game>
{
	public AssistsStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		for (CombatComponent log : event.GetLog().GetAttackers())
		{
			if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller()))
				continue;

			Player player = UtilPlayer.searchExact(log.GetName());

			if (player != null)
			{
				addStat(player, "Assists", 1, false, false);

//				if (getGame().GetKit(player) != null)
//					addStat(player, getGame().GetKit(player).GetName() + " Assists", 1, false, false);
			}
		}
	}
}
