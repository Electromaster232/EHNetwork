package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitUndead;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class BloodThirstyStatTracker extends StatTracker<Game>
{
	private final Map<UUID, Integer> _kills = new HashMap<>();

	public BloodThirstyStatTracker(Game game)
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

		if (getGame().GetKit(player) instanceof KitUndead)
		{
			Integer kills = _kills.get(killer.getUniqueId());

			kills = (kills == null ? 0 : kills) + 1;

			_kills.put(killer.getUniqueId(), kills);

			if (kills >= 50)
				addStat(killer, "BloodThirsty", 1, true, false);
		}
	}
}
