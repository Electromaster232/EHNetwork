package ehnetwork.game.microgames.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.draw.Draw;
import ehnetwork.game.microgames.game.games.draw.DrawRoundEndEvent;

public class KeenEyeStatTracker extends StatTracker<Draw>
{
	private final List<Player> _guessAll = new ArrayList<>();

	public KeenEyeStatTracker(Draw game)
	{
		super(game);
	}

	@EventHandler
	public void onGameStart(GameStateChangeEvent event)
	{
		if (event.GetState() != Game.GameState.Prepare)
			return;

		for (Player player : event.GetGame().GetPlayers(true))
			_guessAll.add(player);
	}
	
	@EventHandler
	public void onDrawRoundEnd(DrawRoundEndEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		for (Iterator<Player> it = _guessAll.iterator(); it.hasNext(); )
		{
			Player player = it.next();
			if (player == event.getDrawRound().Drawer)
				continue;

			if (!event.getDrawRound().Guessed.contains(player.getName()))
				it.remove();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() == Game.GameState.End)
		{
			for (Player player : _guessAll)
				addStat(player, "KeenEye", 1, true, false);
		}
	}
}
