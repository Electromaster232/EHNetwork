package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.minestrike.MineStrike;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class MineStrikeLastAliveKillStatTracker extends StatTracker<MineStrike>
{
	private final Map<UUID, Integer> _killCount = new HashMap<>();

	public MineStrikeLastAliveKillStatTracker(MineStrike game)
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

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)
			return;

		Integer killCount = _killCount.get(player.getUniqueId());
		_killCount.put(player.getUniqueId(), (killCount == null ? 0 : killCount) + 1);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onRoundOver(MineStrike.RoundOverEvent event)
	{
		for (GameTeam team : getGame().GetTeamList())
		{
			List<Player> players = team.GetPlayers(true);

			if (players.size() == 1)
			{
				Player player = players.get(0);
				Integer killCount = _killCount.get(player.getUniqueId());

				if (killCount != null && killCount >= 3)
					addStat(player, "ClutchOrKick", 1, true, false);
			}
		}

		_killCount.clear();
	}
}
