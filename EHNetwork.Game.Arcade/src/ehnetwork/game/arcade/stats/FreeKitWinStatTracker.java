package ehnetwork.game.arcade.stats;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.kit.KitAvailability;

public class FreeKitWinStatTracker extends StatTracker<Game>
{
	public FreeKitWinStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			List<Player> winners = getGame().getWinners();

			if (winners != null)
			{
				for (Player winner : winners)
				{
					if (getGame().GetKit(winner) != null)
					{
						if (getGame().GetKit(winner).GetAvailability() == KitAvailability.Free)
							addStat(winner, "FreeKitsForever", 1, false, false);
					}
				}
			}
		}
	}
}
