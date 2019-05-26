package nautilus.game.arcade.game.games.draw.tools;

import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import nautilus.game.arcade.game.games.draw.Draw;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

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
