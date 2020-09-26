package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.deathtag.kits.AbstractKitChaser;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class ComeAtMeBroStatTracker extends StatTracker<Game>
{
	private final Map<UUID, Integer> _count = new HashMap<>();

	public ComeAtMeBroStatTracker(Game game)
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

		if (getGame().GetKit(player) instanceof AbstractKitChaser)
		{
			Integer count = _count.get(killer.getUniqueId());

			count = (count == null ? 0 : count) + 1;

			_count.put(killer.getUniqueId(), count);

			if (count >= 2)
				addStat(killer, "ComeAtMeBro", 1, true, false);
		}
	}
}
