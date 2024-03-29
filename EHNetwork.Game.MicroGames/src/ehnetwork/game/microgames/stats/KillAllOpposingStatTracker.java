package ehnetwork.game.microgames.stats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class KillAllOpposingStatTracker extends StatTracker<TeamGame>
{
	private final Map<UUID, Set<UUID>> _kills = new HashMap<>();

	public KillAllOpposingStatTracker(TeamGame game)
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

		Player killed = UtilPlayer.searchExact(event.GetLog().GetPlayer().GetName());
		if (killed == null)
			return;

		Set<UUID> kills = _kills.get(killer.getUniqueId());
		if (kills == null)
		{
			kills = new HashSet<>();
			_kills.put(killer.getUniqueId(), kills);
		}

		kills.add(killed.getUniqueId());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			for (GameTeam team : getGame().GetTeamList())
			{
				for (Player player : team.GetPlayers(false))
				{
					Set<UUID> kills = _kills.get(player.getUniqueId());
					if (kills == null)
						continue;

					for (GameTeam otherTeam : getGame().GetTeamList())
					{
						if (otherTeam == team)
							continue;

						boolean killedAll = true;

						for (Player otherPlayer : otherTeam.GetPlayers(true))
						{
							if (!kills.contains(otherPlayer.getUniqueId()))
							{
								killedAll = false;

								break;
							}
						}

						if (killedAll)
							addStat(player, "Ace", 1, true, false);
					}
				}
			}
		}
	}
}
