package ehnetwork.game.arcade.stats;

import org.bukkit.event.EventHandler;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.castlesiege.CastleSiege;

public class KingSlayerStatTracker extends StatTracker<Game>
{
	public KingSlayerStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler
	public void onKingSlaughtered(CastleSiege.KingSlaughterEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		addStat(event.getPlayer(), "KingSlayer", 1, true, false);
	}
}
