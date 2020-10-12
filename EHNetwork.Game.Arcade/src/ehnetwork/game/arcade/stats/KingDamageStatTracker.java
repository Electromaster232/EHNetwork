package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.castlesiege.CastleSiege;

public class KingDamageStatTracker extends StatTracker<CastleSiege>
{
	private final Map<UUID, Double> _kingDamage = new HashMap<>();
	private double _totalKingDamage = 0;

	public KingDamageStatTracker(CastleSiege game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onKingDamage(CastleSiege.KingDamageEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		_totalKingDamage += event.getDamage();

		Double damage = _kingDamage.get(event.getPlayer().getUniqueId());

		damage = (damage == null ? 0 : damage) + event.getDamage();

		_kingDamage.put(event.getPlayer().getUniqueId(), damage);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			for (Player player : getGame().GetPlayers(false))
			{
				Double damage = _kingDamage.get(player.getUniqueId());

				if (damage != null && damage >= 0.5 * _totalKingDamage)
					addStat(player, "Assassin", 1, true, false);
			}
		}
	}
}
