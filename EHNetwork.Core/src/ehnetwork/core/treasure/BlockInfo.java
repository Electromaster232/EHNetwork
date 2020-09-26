package ehnetwork.core.treasure;

import org.bukkit.block.Block;

/**
 * Created by Shaun on 8/28/2014.
 */
public class BlockInfo
{
	private Block _block;

	private int _id;
	private byte _data;

	public BlockInfo(Block block)
	{
		_block = block;
		_id = block.getTypeId();
		_data = block.getData();
	}

	public Block getBlock()
	{
		return _block;
	}

	public int getId()
	{
		return _id;
	}

	public byte getData()
	{
		return _data;
	}
}
