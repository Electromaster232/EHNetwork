package mineplex.core.treasure.animation;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;

import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.treasure.Treasure;

/**
 * Created by shaun on 2014-09-09.
 */
public class LootUncommonAnimation extends Animation
{
	/**
	 * Played when an "Uncommon" chest is opened
	 */

	private Random _random = new Random();
	private Block _block;

	public LootUncommonAnimation(Treasure treasure, Block block)
	{
		super(treasure);

		_block = block;
	}

	@Override
	protected void tick()
	{
		if (getTicks() >= 10)
			finish();

		if (getTicks() == 10)
		{
			UtilFirework.playFirework(_block.getLocation().add(0.5, 0.5, 0.5), Type.BURST, Color.AQUA, false, false);
		}
		else if (getTicks() % 2 == 0)
		{
			UtilParticle.PlayParticle(UtilParticle.ParticleType.HEART, _block.getLocation().add(0.5, 1.2, 0.5), 0.5F, 0.2F, 0.5F, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
