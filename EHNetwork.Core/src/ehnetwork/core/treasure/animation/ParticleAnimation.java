package ehnetwork.core.treasure.animation;

import java.util.ArrayList;

import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.treasure.Treasure;

/**
 * Created by Shaun on 8/29/2014.
 */
public class ParticleAnimation extends Animation
{
	private static double MODIFIER = 0.5;
	private static ArrayList<Vector> PATH = new ArrayList<Vector>();

	static
	{
		double y = 5;
		double x = 3;
		double z = -3;

		for (z = -3; z <= 3; z += MODIFIER)
		{
			PATH.add(new Vector(x, y, z));
		}

		for (x = 3; x >= -3; x -= MODIFIER)
		{
			PATH.add(new Vector(x, y, z));
		}

		for (z = 3; z >= -3; z -= MODIFIER)
		{
			PATH.add(new Vector(x, y, z));
		}

		for (x = -3; x <= 3; x += MODIFIER)
		{
			PATH.add(new Vector(x, y, z));
		}
	}

	private int pathPosition = 0;

	public ParticleAnimation(Treasure treasure)
	{
		super(treasure);
	}

	@Override
	protected void tick()
	{
		Vector position = PATH.get(pathPosition);

		UtilParticle.PlayParticle(getTreasure().getTreasureType().getStyle().getSecondaryParticle(), getTreasure().getCenterBlock().getLocation().add(0.5, 0, 0.5).add(position), 0, 0, 0, 0, 1,
				UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());

		pathPosition = (pathPosition + 1) % PATH.size();
	}

	@Override
	protected void onFinish()
	{

	}

}
