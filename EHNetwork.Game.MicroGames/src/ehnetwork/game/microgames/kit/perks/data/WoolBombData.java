package ehnetwork.game.microgames.kit.perks.data;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class WoolBombData
{
	public Block Block;
	public long Time;
	public Material Material;
	public byte Data;
	
	public WoolBombData(Block block)
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
