package ehnetwork.game.microgames.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.tug.turfforts.TurfForts;

public class BehindEnemyLinesStatTracker extends StatTracker<TurfForts>
{
	public BehindEnemyLinesStatTracker(TurfForts game)
	{
		super(game);
	}

	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (event.getType() == UpdateType.SEC)
		{
			for (Player player : getGame().GetPlayers(true))
			{
				Long time = getGame().getEnemyTurfEntranceTime(player);

				if (time != null && System.currentTimeMillis() - time >= 15000)
					addStat(player, "BehindEnemyLines", 1, true, false);
			}
		}
	}
}
