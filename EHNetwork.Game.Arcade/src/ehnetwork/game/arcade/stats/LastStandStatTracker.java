package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class LastStandStatTracker extends StatTracker<TeamGame>
{
	private final Map<UUID, Integer> _kills = new HashMap<>();

	public LastStandStatTracker(TeamGame game)
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

		if (getGame().GetTeam(killer).GetPlayers(true).size() == 1)
		{
			Integer kills = _kills.get(killer.getUniqueId());

			kills = (kills == null ? 0 : kills) + 1;

			_kills.put(killer.getUniqueId(), kills);

			if (kills >= 3)
				addStat(killer, "LastStand", 1, true, false);
		}
	}
}
