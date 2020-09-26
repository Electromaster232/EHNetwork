package ehnetwork.core.treasure.animation;

import java.util.List;
import java.util.Random;

import ehnetwork.core.treasure.BlockInfo;
import ehnetwork.core.treasure.Treasure;

/**
 * Created by shaun on 14-09-15.
 */
public class TreasureRemoveAnimation extends Animation
{
	private Random _random = new Random();
	private List<BlockInfo> _openedChests;
	private List<BlockInfo> _otherChests;

	public TreasureRemoveAnimation(Treasure treasure, List<BlockInfo> openedChests, List<BlockInfo> otherChests)
	{
		super(treasure);
		_openedChests = openedChests;
		_otherChests = otherChests;
	}

	@Override
	protected void tick()
	{
		if (getTicks() >= 20 && getTicks() % 10 == 0)
		{
			if (!_otherChests.isEmpty())
			{
				BlockInfo info = _otherChests.remove(_random.nextInt(_otherChests.size()));
				getTreasure().resetBlockInfo(info);
//				System.out.println("otherchests");
			}
//			else if (!_openedChests.isEmpty())
//			{
//				System.out.println("openchests");
//				BlockInfo info = _openedChests.remove(_random.nextInt(_openedChests.size()));
//				getTreasure().resetBlockInfo(info);
//			}
			else
			{
				finish();
			}
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
