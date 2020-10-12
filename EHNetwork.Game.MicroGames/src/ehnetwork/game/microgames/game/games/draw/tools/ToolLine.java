package ehnetwork.game.microgames.game.games.draw.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.game.microgames.game.games.draw.Draw;

public class ToolLine extends Tool
{
	public ToolLine(Draw host)
	{
		super(host, Material.STONE_SWORD);
	}

	public void customDraw(Block end)
	{
		Location loc = _start.getLocation().add(0.5, 0.5, 0.5);
		
		while (UtilMath.offset(loc, end.getLocation().add(0.5, 0.5, 0.5)) > 0.5)
		{					
			loc.add(UtilAlg.getTrajectory(loc, end.getLocation().add(0.5, 0.5, 0.5)).multiply(0.5));

			Block lineBlock = loc.getBlock();
			
			if (_new.containsKey(lineBlock))
				continue;
			
			if (!Host.getCanvas().contains(lineBlock))
				continue;
			
			byte color = lineBlock.getData();
			if (_past.containsKey(lineBlock))
				color = _past.get(lineBlock);
			
			_new.put(lineBlock, color);
			lineBlock.setData(Host.getColor());
		}
	}
}
