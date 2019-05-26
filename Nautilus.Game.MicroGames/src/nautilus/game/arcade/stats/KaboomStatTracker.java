package nautilus.game.arcade.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import nautilus.game.arcade.game.games.minestrike.MineStrike;
import nautilus.game.arcade.game.games.minestrike.items.grenades.Grenade;
import nautilus.game.arcade.game.games.minestrike.items.grenades.HighExplosive;

public class KaboomStatTracker extends StatTracker<MineStrike>
{
	public KaboomStatTracker(MineStrike game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGrenadeExplode(Grenade.GrenadeExplodeEvent event)
	{
		if (event.getGrenade() instanceof HighExplosive)
		{
			if (event.getDamagedPlayers().size() >= 2)
			{
				for (Player player : event.getDamagedPlayers())
				{
					if (player.getHealth() != player.getMaxHealth())
						return;
				}

				addStat(event.getThrower(), "Kaboom", 1, true, false);
			}
		}
	}
}
