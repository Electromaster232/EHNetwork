package ehnetwork.game.arcade.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.hideseek.HideSeek;

public class MeowStatTracker extends StatTracker<Game>
{
	private final Map<UUID, Integer> _meowCount = new HashMap<>();

	public MeowStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onMeow(HideSeek.MeowEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		Integer meows = _meowCount.get(event.getPlayer().getUniqueId());

		meows = (meows == null ? 0 : meows) + 1;

		_meowCount.put(event.getPlayer().getUniqueId(), meows);

		if (meows >= 50)
			addStat(event.getPlayer(), "Meow", 1, true, false);
	}
}
