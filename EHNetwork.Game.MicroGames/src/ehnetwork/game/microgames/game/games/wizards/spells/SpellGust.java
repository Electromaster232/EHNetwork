package ehnetwork.game.microgames.game.games.wizards.spells;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.games.wizards.Spell;
import ehnetwork.game.microgames.game.games.wizards.spellinterfaces.SpellClick;

public class SpellGust extends Spell implements SpellClick
{

	@Override
	public void castSpell(final Player player)
	{
		final Vector vector = player.getLocation().getDirection().setY(0).normalize().multiply(1.5).setY(0.3)
				.multiply(1.2 + (getSpellLevel(player) * 0.4D));

		final double gustSize = (getSpellLevel(player) * 3) + 10;
		
		final HashMap<Player, Double> effected = UtilPlayer.getPlayersInPyramid(player, 45, gustSize);

		Iterator<Entry<Player, Double>> itel = effected.entrySet().iterator();

		while (itel.hasNext())
		{
			Entry<Player, Double> entry = itel.next();

			if (!entry.getKey().canSee(player))
			{
				itel.remove();
			}
		}

		if (!effected.isEmpty())
		{
			charge(player);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Wizards.getArcadeManager().getPlugin(), new Runnable()
			{
				public void run()
				{
					for (Player target : effected.keySet())
					{
						if (!Wizards.IsAlive(target))
						{
							continue;
						}

						Vector vec = vector.clone().multiply(Math.max(0.2, effected.get(target)));

						Wizards.getArcadeManager().GetCondition().Factory().Falling("Gust", target, player, 40, false, true);

						target.setVelocity(vec);

						target.getWorld().playSound(target.getLocation(), Sound.BAT_TAKEOFF, 1, 0.7F);
					}

					player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 0.7F);
				}
			});
		}
	}
}
