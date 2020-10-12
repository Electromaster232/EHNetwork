package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class DeathsStatTracker extends StatTracker<Game>
{
	public DeathsStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (event.GetLog().GetPlayer() == null)
			return;

		if (!event.GetLog().GetPlayer().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetPlayer().GetName());
		if (player == null)
			return;

		addStat(player, "Deaths", 1, false, false);

//		if (getGame().GetKit(player) != null)
//			addStat(player, getGame().GetKit(player).GetName() + " Deaths", 1, false, false);
	}
}
