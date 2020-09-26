package ehnetwork.game.arcade.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;

public class WinWithoutOpeningChestStatTracker extends StatTracker<Game>
{
	private final Set<String> _openChest = new HashSet<String>();

	public WinWithoutOpeningChestStatTracker(Game game)
	{
		super(game);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onOpenChest(PlayerInteractEvent event) 
	{
		if (event.isCancelled())
		{
			return;
		}
		if (UtilEvent.isAction(event, ActionType.R_BLOCK))
		{
			if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST)
			{
				_openChest.add(event.getPlayer().getUniqueId().toString());
			}
		}
	}
	
	@EventHandler
	public void onBreakChest(BlockBreakEvent event)
	{
		if (event.getBlock().getType() == Material.CHEST)
		{
			_openChest.add(event.getPlayer().getUniqueId().toString());
		}
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
					if (!_openChest.contains(winner.getUniqueId().toString()))
					{
						addStat(winner, "NoChest", 1, true, false);
					}
				}
			}
		}
	}

}
