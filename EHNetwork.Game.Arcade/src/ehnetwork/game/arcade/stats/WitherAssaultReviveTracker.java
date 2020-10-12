package ehnetwork.game.arcade.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.wither.events.HumanReviveEvent;

public class WitherAssaultReviveTracker extends StatTracker<Game>
{
	public WitherAssaultReviveTracker(Game game)
	{
		super(game);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCombatDeath(HumanReviveEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getPlayer(), "WitherHeal", 1, false, false);
	}

}
