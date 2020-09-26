package ehnetwork.core.treasure.animation;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.treasure.Treasure;

/**
 * Created by shaun on 14-09-12.
 */
public class LootMythicalAnimation extends Animation
{
	/**
	 * Played when a "Very Rare" chest is opened
	 */

	private Random _random = new Random();
	private Block _chestBlock;

	public LootMythicalAnimation(Treasure treasure, Block chestBlock)
	{
		super(treasure);

		_chestBlock = chestBlock;
	}

	@Override
	protected void tick()
	{
		if (getTicks() < 30 && getTicks() % 3 == 0)
		{
			UtilFirework.playFirework(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Type.BALL_LARGE, Color.RED, true, true);
		}
		
		if (getTicks() == 1)
		{
			_chestBlock.getLocation().getWorld().playSound(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Sound.PORTAL_TRAVEL, 10F, 2.0F);
			_chestBlock.getLocation().getWorld().playSound(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Sound.ZOMBIE_UNFECT, 10F, 0.1F);
		}
		else if (getTicks() < 60)
		{
			UtilFirework.launchFirework(_chestBlock.getLocation().add(0.5, 0.5, 0.5), Type.BALL_LARGE, Color.RED, true, true, 
					new Vector((Math.random()-0.5)*0.05, 0.1, (Math.random()-0.5)*0.05), 1);
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
