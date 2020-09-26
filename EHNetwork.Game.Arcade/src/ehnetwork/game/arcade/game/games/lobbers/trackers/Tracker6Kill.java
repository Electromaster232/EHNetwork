package ehnetwork.game.arcade.game.games.lobbers.trackers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.game.games.lobbers.BombLobbers;
import ehnetwork.game.arcade.stats.StatTracker;

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
