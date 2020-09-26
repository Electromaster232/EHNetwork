package ehnetwork.game.microgames.game.games.wizards.spells;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.game.microgames.game.games.wizards.Spell;
import ehnetwork.game.microgames.game.games.wizards.spellinterfaces.SpellClick;

public class SpellWebShot extends Spell implements SpellClick, IThrown
{

	@Override
	public void castSpell(final Player player)
	{
		shoot(player);

		for (int i = 1; i < getSpellLevel(player) * 2; i++)
		{

			Bukkit.getScheduler().scheduleSyncDelayedTask(Wizards.getArcadeManager().getPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					shoot(player);
				}

			}, i * 10);
		}

		charge(player);
	}

	private void shoot(Player player)
	{

		if (Wizards.IsAlive(player))
		{
			org.bukkit.entity.Item ent = player.getWorld().dropItem(
					player.getEyeLocation(),
					ItemStackFactory.Instance.CreateStack(Material.WEB, (byte) 0, 1,
							"Web " + player.getName() + " " + System.currentTimeMillis()));

			UtilAction.velocity(ent, player.getLocation().getDirection(), 1.5, false, 0, 0.2, 10, false);
			Wizards.getArcadeManager().GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 2f);

			player.getWorld().playSound(player.getLocation(), Sound.CLICK, 1.2F, 0.8F);
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target != data.GetThrower())
		{
			Location loc = data.GetThrown().getLocation();

			if (target != null)
			{
				Location l = target.getLocation();

				l.setY(loc.getY());

				if (!UtilBlock.airFoliage(getValidLocation(l)))
				{
					l = target.getLocation().add(0, UtilMath.random.nextFloat(), 0);

					if (UtilBlock.airFoliage(getValidLocation(l)))
					{
						loc = l;
					}
				}
				else
				{
					loc = l;
				}

				// Damage Event
				/*	Wizards.getArcadeManager()
							.GetDamage()
							.NewDamageEvent(target, data.GetThrower(), null, DamageCause.PROJECTILE, 2, false, false, false,
									"Web Shot", "Web Shot");*/
			}

			Web(data, loc);
		}
	}

	private Block getValidLocation(Location loc)
	{
		double[] doubles = new double[]
			{
					0, -0.5, 0.5
			};

		HashMap<Block, Integer> commonBlocks = new HashMap<Block, Integer>();
		int most = 0;

		for (double x : doubles)
		{
			for (double y : doubles)
			{
				for (double z : doubles)
				{
					Block b = loc.clone().add(x, y, z).getBlock();

					if (UtilBlock.airFoliage(b))
					{
						int amount = (commonBlocks.containsKey(b) ? commonBlocks.get(b) : 0) + 1;

						commonBlocks.put(b, amount);

						if (amount > most)
						{
							most = amount;
						}
					}
				}
			}
		}

		for (Entry<Block, Integer> entry : commonBlocks.entrySet())
		{
			if (entry.getValue() == most)
			{
				return entry.getKey();
			}
		}

		return loc.getBlock();
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		Web(data, data.GetThrown().getLocation());
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		Web(data, data.GetThrown().getLocation());
	}

	public void Web(ProjectileUser data, Location loc)
	{
		data.GetThrown().remove();

		Block block = getValidLocation(loc);

		if (UtilBlock.airFoliage(block))
			block.setType(Material.WEB);
	}
}
