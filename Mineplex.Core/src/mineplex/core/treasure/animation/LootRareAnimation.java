package mineplex.core.treasure.animation;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;

import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.treasure.Treasure;

/**
 * Created by shaun on 2014-09-09.
 */
public class LootRareAnimation extends Animation
{
	/**
	 * Played when a "Rare" chest is opened
	 */

	private Location _centerLocation;

	public LootRareAnimation(Treasure treasure, Location centerLocation)
	{
		super(treasure);

		_centerLocation = centerLocation;
	}

	@Override
	protected void tick()
	{
		if (getTicks() == 2)
		{
			UtilFirework.playFirework(_centerLocation, Type.BALL, Color.FUCHSIA, false, false);
			
			_centerLocation.getWorld().playSound(_centerLocation, Sound.WITHER_SPAWN, 10F, 1.2F);
		}
		else if (getTicks() >= 60)
		{
			finish();
		}

		//Particle Ground
		{
			double currentRotation = getTicks() / 20D;
			double radius = currentRotation;
			double yDiff = currentRotation;
			double xDiff = Math.sin(currentRotation * 2 * Math.PI) * radius;
			double zDiff = Math.cos(currentRotation * 2 * Math.PI) * radius;

			Location location = _centerLocation.clone().add(xDiff, yDiff, zDiff);

			UtilParticle.PlayParticle(UtilParticle.ParticleType.WITCH_MAGIC, location, 0, 0, 0, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
		}

		//Particle Spiral Up
		double radius = getTicks() / 20D;
		int particleAmount = getTicks() / 2;
		for (int i = 0; i < particleAmount; i++)
		{
			double xDiff = Math.sin(i/(double)particleAmount * 2 * Math.PI) * radius;
			double zDiff = Math.cos(i/(double)particleAmount * 2 * Math.PI) * radius;

			Location location = _centerLocation.clone().add(xDiff, -1.3, zDiff);
			UtilParticle.PlayParticle(UtilParticle.ParticleType.WITCH_MAGIC, location, 0, 0, 0, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
