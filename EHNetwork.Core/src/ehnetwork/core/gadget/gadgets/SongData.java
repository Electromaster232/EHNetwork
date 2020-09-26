package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.block.Block;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;

public class SongData
{
	public Block Block;
	public long EndTime;
	
	public SongData(Block block, long duration)
	{
		Block = block;
		EndTime = System.currentTimeMillis() + duration;
		
		Block.setType(Material.JUKEBOX);
	}
	
	public boolean update()
	{
		if (System.currentTimeMillis() > EndTime)
		{
			if (Block.getType() == Material.JUKEBOX)
				Block.setType(Material.AIR);
			
			return true;
		}
			
		UtilParticle.PlayParticle(UtilParticle.ParticleType.NOTE, Block.getLocation().add(0.5, 1, 0.5), 0.5f, 0.5f, 0.5f, 0f, 2,
				UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());

		return false;
	}
}
