package ehnetwork.game.arcade.game.games.lobbers.trackers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.stats.StatTracker;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class TrackerNoDamage extends StatTracker<Game>
{
	private Set<String> _damaged = new HashSet<String>();
	
	public TrackerNoDamage(Game game)
	{
		super(game);
	}
	
	@EventHandler
	public void onGameEnd(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.End)
			return;
		
		if (event.GetGame().getWinners() == null)
			return;
		
		for (Player player : event.GetGame().getWinners())
		{
			if (_damaged.contains(player.getName()))
				continue;
			
			addStat(player, "JellySkin", 1, true, false);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDamage(CustomDamageEvent event)
	{
		if (!getGame().IsLive())
			return;

		if (event.GetDamageePlayer() == null || !event.GetDamageePlayer().isOnline())
			return;
		
		if (!_damaged.contains(event.GetDamageePlayer().getName()))
			_damaged.add(event.GetDamageePlayer().getName());
	}
}