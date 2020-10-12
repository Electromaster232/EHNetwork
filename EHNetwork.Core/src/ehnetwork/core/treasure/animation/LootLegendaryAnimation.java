package ehnetwork.core.treasure.animation;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.treasure.Treasure;

/**
 * Created by shaun on 14-09-12.
 */
public class LootLegendaryAnimation extends Animation
{
	/**
	 * Played when a "Very Rare" chest is opened
	 */

	private Random _random = new Random();
	private Block _chestBlock;

	public LootLegendaryAnimation(Treasure treasure, Block chestBlock)
	{
		super(treasure);

		_chestBlock = chestBlock;
	}

	@Override
	protected void tick()
	{
		if (getTicks() < 12 && getTicks() % 3 == 0)
		{
			UtilFirework.playFirework(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Type.BALL_LARGE, Color.LIME, true, true);
		}
		
		if (getTicks() == 1)
		{
			_chestBlock.getLocation().getWorld().playSound(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Sound.ENDERDRAGON_DEATH, 10F, 2.0F);
		}
		else if (getTicks() < 35)
		{
			double radius = 2 - (getTicks() / 10D * 2);
			int particleAmount = 20 - (getTicks() * 2);
			Location _centerLocation = _chestBlock.getLocation().add(0.5, 0.1, 0.5);
			for (int i = 0; i < particleAmount; i++)
			{
				double xDiff = Math.sin(i/(double)particleAmount * 2 * Math.PI) * radius;
				double zDiff = Math.cos(i/(double)particleAmount * 2 * Math.PI) * radius;

				Location location = _centerLocation.clone().add(xDiff, 0, zDiff);
				UtilParticle.PlayParticle(UtilParticle.ParticleType.HAPPY_VILLAGER, location, 0, 0, 0, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
		else if (getTicks() < 40)
		{
			double xDif = _random.nextGaussian() * 0.5;
			double zDif = _random.nextGaussian() * 0.5;
			double yDif = _random.nextGaussian() * 0.5;

			Location loc = _chestBlock.getLocation().add(0.5, 0.5, 0.5).add(xDif, zDif, yDif);

			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0F);
		}
		else
		{
			finish();
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
