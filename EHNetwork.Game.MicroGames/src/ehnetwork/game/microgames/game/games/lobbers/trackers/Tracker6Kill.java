package ehnetwork.game.microgames.game.games.lobbers.trackers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.game.games.lobbers.BombLobbers;
import ehnetwork.game.microgames.stats.StatTracker;

public class Tracker6Kill extends StatTracker<Game>
{
	public Tracker6Kill(Game game)
	{
		super(game);
	}
	
	@EventHandler
	public void onEndgame(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.End)
			return;
		
		if (getGame() instanceof BombLobbers)
		{
			for (Player player : UtilServer.getPlayers())
			{
				if (((BombLobbers) getGame()).getKills(player) >= 6.0)
				{
					addStat(player, "Killer", 1, true, false);
				}
			}
		}
	}
}
