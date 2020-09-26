package ehnetwork.game.arcade.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class WinWithoutDyingStatTracker extends StatTracker<Game>
{
	private final Set<String> _hasDied = new HashSet<String>();
	private final String _stat;

	public WinWithoutDyingStatTracker(Game game, String stat)
	{
		super(game);

		_stat = stat;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(CombatDeathEvent event)
	{
		if (!getGame().IsLive())
			return;

		if (event.GetLog().GetPlayer() == null)
			return;

		if (!event.GetLog().GetPlayer().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetPlayer().GetName());
		if (player == null || !player.isOnline())
		{
			return;
		}
		_hasDied.add(player.getUniqueId().toString());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			List<Player> winners = getGame().getWinners();

			if (winners != null)
			{
				for (Player winner : winners)
				{
					if (!_hasDied.contains(winner.getUniqueId().toString()))
					{
						addStat(winner, _stat, 1, true, false);
					}
				}
			}
		}
	}

	public String getStat()
	{
		return _stat;
	}
}
