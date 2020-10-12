package ehnetwork.core.mount.types;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.mount.HorseMount;
import ehnetwork.core.mount.MountManager;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MountFrost extends HorseMount
{
	public MountFrost(MountManager manager)
	{
		super(manager, "Glacial Steed", new String[]
				{
					C.cWhite + "Born in the North Pole,",
					C.cWhite + "it leaves a trail of frost",
					C.cWhite + "as it moves!",
				},
				Material.SNOW_BALL,
				(byte)0,
				15000,
				Color.WHITE, Style.WHITE, Variant.HORSE, 1, null);
	}
	
	
	@EventHandler
	public void Trail(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
			for (Horse horse : GetActive().values())
				UtilParticle.PlayParticle(UtilParticle.ParticleType.SNOW_SHOVEL, horse.getLocation().add(0, 1, 0),
						0.25f, 0.25f, 0.25f, 0.1f, 4,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
	}
	
//	@EventHandler 
//	public void SnowAura(UpdateEvent event) 
//	{
//		if (event.getType() != UpdateType.TICK)
//			return;
//
//		for (Horse horse : GetActive().values())
//		{	
//			//Blocks
//			double duration = 2000;
//			HashMap<Block, Double> blocks = UtilBlock.getInRadius(horse.getLocation(), 2.5d);
//						
//			for (Iterator<Entry<Block, Double>> blockIterator = blocks.entrySet().iterator(); blockIterator.hasNext();)
//			{
//				Block block = blockIterator.next().getKey();
//				HashMap<Block, Double> snowBlocks = UtilBlock.getInRadius(block.getLocation(), 2d);
//			
//				boolean addSnow = true;
//				
//				for (Block surroundingBlock : snowBlocks.keySet())
//				{
//					if (surroundingBlock.getType() == Material.PORTAL || surroundingBlock.getType() == Material.CACTUS)
//					{
//						blockIterator.remove();
//						addSnow = false;
//						break;
//					}
//				}
//
//				
//				if (addSnow)
//					Manager.getBlockRestore().Snow(block, (byte)1, (byte)1, (long)(duration * (1 + blocks.get(block))), 250, 0);
//			}
//		}
//	}
}
