package ehnetwork.game.microgames.stats;

import org.bukkit.event.EventHandler;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.castlesiege.CastleSiege;

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
