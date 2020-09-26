package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class TeamKillsStatTracker extends StatTracker<Game>
{
	public TeamKillsStatTracker(Game game)
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

		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)
			return;

		GameTeam team = getGame().GetTeam(player);

		if (team != null && team.GetName() != null)
			addStat(player, team.GetName() + " Kills", 1, false, false);
	}
}
