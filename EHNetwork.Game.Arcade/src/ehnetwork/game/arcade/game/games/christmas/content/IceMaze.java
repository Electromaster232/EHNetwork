package ehnetwork.game.arcade.game.games.christmas.content;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.game.arcade.game.games.christmas.Christmas;

public class IceMaze 
{
	private Christmas Host;
	
	private ArrayList<Location> _corners;
	private ArrayList<Block> _blocks;
	private ArrayList<Location> _exits;
	
	private Location _present;
	
	public IceMaze(Christmas host, ArrayList<Location> mazeCorners, ArrayList<Location> mazeExits, Location[] presents)
	{
		Host = host;
		
		_corners = mazeCorners;
		_exits = mazeExits;
			
		//Set Present	
		if (UtilMath.offset(presents[0], _exits.get(0)) < UtilMath.offset(presents[1], _exits.get(0)))
		{
			_present = presents[0].getBlock().getLocation();
		}
		else
		{
			_present = presents[1].getBlock().getLocation();
		}
		
		for (Location loc : _corners)
			loc.getBlock().setType(Material.AIR);
		
		_blocks = UtilBlock.getInBoundingBox(_corners.get(0), _corners.get(1));
		
		
		
		//Exits
		Location exit = UtilAlg.Random(_exits);
		for (Location loc : _exits)
		{
			if (UtilMath.offset(loc, exit) < 3)
			{
				loc.getBlock().setType(Material.AIR);
			}
			else
			{
				loc.getBlock().setType(Material.ICE);
			}
		}
	}

	public void Update()
	{
		//No Presents
		if (!Host.GetSleigh().HasPresent(_present))
			return;
		
		//Finished
		if (_blocks.isEmpty())
			return;
		
		for (int i=0 ; i<20 ; i++)
		{
			if (_blocks.isEmpty())
				break;
			
			Block block = _blocks.remove(_blocks.size() - 1);
			
			if (block.getType() == Material.ICE)
				block.setType(Material.AIR);
		}
	}
}
