package ehnetwork.mapparser;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockData
{
	public Block Block;
	public Material Material;
	public byte Data;
	public long Time;
	
	public BlockData(Block block)
	{
		Block = block;
		Material = block.getType();
		Data = block.getData();
		Time = System.currentTimeMillis();
	}
	
	public void restore()
	{
		Block.setTypeIdAndData(Material.getId(), Data, true);
	}
}
