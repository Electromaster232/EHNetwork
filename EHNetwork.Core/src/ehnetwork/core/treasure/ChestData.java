package ehnetwork.core.treasure;

import org.bukkit.block.Block;

public class ChestData
{
	private Block _block;
	private boolean _opened;

	public ChestData(Block block)
	{
		_block = block;
		_opened = false;
	}

	public boolean isOpened()
	{
		return _opened;
	}

	public void setOpened(boolean opened)
	{
		_opened = opened;
	}

	public Block getBlock()
	{
		return _block;
	}

}
