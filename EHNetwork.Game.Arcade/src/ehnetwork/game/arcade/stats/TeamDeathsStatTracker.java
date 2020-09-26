package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class TeamDeathsStatTracker extends StatTracker<Game>
{
	public TeamDeathsStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (getGame().GetTeamList().size() < 2)
			return;

		if (event.GetLog().GetPlayer() == null)
			return;

		if (!event.GetLog().GetPlayer().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetPlayer().GetName());
		if (player == null)
			return;

		GameTeam team = getGame().GetTeam(player);

		if (team != null && team.GetName() != null)
			addStat(player, team.GetName() + " Deaths", 1, false, false);
	}
}
