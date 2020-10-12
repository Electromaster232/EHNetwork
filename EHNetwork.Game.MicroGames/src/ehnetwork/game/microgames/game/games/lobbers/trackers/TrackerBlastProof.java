package ehnetwork.game.microgames.game.games.lobbers.trackers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.game.games.lobbers.kits.KitArmorer;
import ehnetwork.game.microgames.stats.StatTracker;

public class TrackerBlastProof extends StatTracker<Game>
{
	
	public TrackerBlastProof(Game game)
	{
		super(game);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameEnd(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.End)
			return;
		
		if (getGame().getWinners() == null)
			return;
		
		for (Player winner : getGame().getWinners())
		{
			if (getGame().GetKit(winner) instanceof KitArmorer)
			{
				addStat(winner, "BlastProof", 1, false, false);
			}
		}
	}
}
