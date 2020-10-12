package ehnetwork.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.tug.turfforts.TurfForts;
import ehnetwork.game.arcade.game.games.tug.turfforts.kits.KitShredder;

public class BlockShreadStatTracker extends StatTracker<Game>
{
	public BlockShreadStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onShredBlock(TurfForts.ShredBlockEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (event.getArrow().getShooter() instanceof Player)
		{
			Player shooter = (Player) event.getArrow().getShooter();

			if (getGame().GetKit(shooter) instanceof KitShredder)
				addStat(shooter, "TheShreddinator", 1, false, false);
		}
	}
}
