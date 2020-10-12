package ehnetwork.core.treasure.animation;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import ehnetwork.core.treasure.BlockInfo;
import ehnetwork.core.treasure.Treasure;
import ehnetwork.core.treasure.TreasureType;

public class BlockChangeAnimation extends Animation
{
	private static final int MAX_RADIUS = 4;

	private int _currentRadius;
	private List<BlockInfo> _blockInfoList;

	public BlockChangeAnimation(Treasure treasure, List<BlockInfo> blockInfoList)
	{
		super(treasure);

		_currentRadius = 0;
		_blockInfoList = blockInfoList;
	}

	@Override
	protected void tick()
	{
		if (_currentRadius == MAX_RADIUS)
		{
			finish();
			return;
		}
		else if (getTicks() % 10 == 0)
		{
			Block centerBlock = getTreasure().getCenterBlock().getRelative(BlockFace.DOWN);

			for (int x = -_currentRadius; x <= _currentRadius; x++)
			{
				for (int y = 0; y <= _currentRadius; y++)
				{
					for (int z = -_currentRadius; z <= _currentRadius; z++)
					{
						Block b = centerBlock.getRelative(x, y, z);
						if (y > 0 && (b.getType() == Material.SMOOTH_BRICK || b.getType() == Material.STEP || b.getType() == Material.SMOOTH_STAIRS))
						{
							_blockInfoList.add(new BlockInfo(b));
							b.setType(Material.AIR);
						}
						else if (b.getType() == Material.SMOOTH_BRICK)
						{
							if (getTreasure().getTreasureType() == TreasureType.OLD)
								continue;
							
							Material newMaterial = getTreasure().getTreasureType() == TreasureType.ANCIENT ? Material.NETHER_BRICK : Material.QUARTZ_BLOCK;
							_blockInfoList.add(new BlockInfo(b));
							b.setType(newMaterial);
						}
						else if (b.getType() == Material.SMOOTH_STAIRS || b.getType() == Material.COBBLESTONE_STAIRS)
						{
							if (getTreasure().getTreasureType() == TreasureType.OLD)
								continue;
							
							Material newMaterial = getTreasure().getTreasureType() == TreasureType.ANCIENT ? Material.NETHER_BRICK_STAIRS : Material.QUARTZ_STAIRS;
							_blockInfoList.add(new BlockInfo(b));
							b.setType(newMaterial);
						}
					}
				}
			}
			_currentRadius++;
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
