package nautilus.game.arcade.stats;

import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.games.skywars.events.PlayerKillZombieEvent;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class SkywarsKillZombieStatTracker extends StatTracker<Game>
{

	public SkywarsKillZombieStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onKillZombie(PlayerKillZombieEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		if (!(event.getWho() instanceof Player))
		{
			return;
		}

		if (!(event.getZombie() instanceof Zombie))
		{
			return;
		}

		addStat(event.getWho(), "ZombieKills", 1, false, false);
	}

}
