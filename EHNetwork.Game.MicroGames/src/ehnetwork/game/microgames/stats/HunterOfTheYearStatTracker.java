package ehnetwork.game.microgames.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.hideseek.kits.KitHider;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class HunterOfTheYearStatTracker extends StatTracker<Game>
{
	private final Map<UUID, Integer> _hidersKilled = new HashMap<>();

	public HunterOfTheYearStatTracker(Game game)
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

		if (getGame().GetKit(player) instanceof KitHider)
		{
			Integer hidersKilled = _hidersKilled.get(killer.getUniqueId());

			hidersKilled = (hidersKilled == null ? 0 : hidersKilled) + 1;

			_hidersKilled.put(killer.getUniqueId(), hidersKilled);

			if (hidersKilled >= 7)
				addStat(killer, "HunterOfTheYear", 1, true, false);
		}
	}
}
