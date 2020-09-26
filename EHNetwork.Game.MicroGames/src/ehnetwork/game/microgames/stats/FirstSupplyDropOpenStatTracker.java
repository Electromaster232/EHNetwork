package ehnetwork.game.microgames.stats;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.survivalgames.SupplyChestOpenEvent;

public class FirstSupplyDropOpenStatTracker extends StatTracker<Game>
{
	private Set<Block> _opened = new HashSet<Block>();

	public FirstSupplyDropOpenStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onSupplyChestOpen(SupplyChestOpenEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (_opened.contains(event.getChest()))
			return;
		
		_opened.add(event.getChest());

		addStat(event.getPlayer(), "SupplyDropsOpened", 1, false, false);
	}
}
