package ehnetwork.game.microgames.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class DeathBomberStatTracker extends StatTracker<Game>
{
	private int _required;
	private final Map<UUID, Integer> _killCount = new HashMap<>();

	public DeathBomberStatTracker(Game game, int requiredKills)
	{
		super(game);
		
		_required = requiredKills;
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

		if (event.GetLog().GetKiller() != null && event.GetLog().GetKiller().GetReason().contains("Throwing TNT"))
		{
			Integer count = _killCount.get(killer.getUniqueId());

			count = (count == null ? 0 : count) + 1;

			System.out.println("Death Bomber: " + killer.getName() + " " + count);
			
			_killCount.put(killer.getUniqueId(), count);

			if (count >= _required)
				addStat(killer, "DeathBomber", 1, true, false);
		}
	}
}
