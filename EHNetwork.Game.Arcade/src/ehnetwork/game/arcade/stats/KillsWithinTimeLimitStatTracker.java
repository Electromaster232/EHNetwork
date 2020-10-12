package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class KillsWithinTimeLimitStatTracker extends StatTracker<Game>
{
	private final int _killCount;
	private final String _stat;
	private final int _timeLimit;

	private final Map<UUID, Integer> _kills = new HashMap<>();

	public KillsWithinTimeLimitStatTracker(Game game, int killCount, int timeLimit, String stat)
	{
		super(game);

		_killCount = killCount;
		_stat = stat;
		_timeLimit = timeLimit * 1000;
	}

	public int getKillCount()
	{
		return _killCount;
	}

	public int getTimeLimit()
	{
		return _timeLimit;
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

		Integer killCount = _kills.get(player.getUniqueId());

		killCount = (killCount == null ? 0 : killCount) + 1;

		_kills.put(player.getUniqueId(), killCount);

		if (killCount == getKillCount() && System.currentTimeMillis() - getGame().GetStateTime() < getTimeLimit())
			addStat(player, getStat(), 1, true, false);
	}

	public String getStat()
	{
		return _stat;
	}
}
