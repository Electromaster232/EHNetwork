package ehnetwork.core.blockrestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class BlockRestore extends MiniPlugin
{
	private HashMap<Block, BlockRestoreData> _blocks = new HashMap<Block, BlockRestoreData>();

	public BlockRestore(JavaPlugin plugin) 
	{
		super("Block Restore", plugin);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void BlockBreak(BlockBreakEvent event)
	{
		if (Contains(event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void BlockPlace(BlockPlaceEvent event)
	{
		if (Contains(event.getBlockPlaced()))
			event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void Piston(BlockPistonExtendEvent event)
	{
		if (event.isCancelled())
			return;

		Block push = event.getBlock();
		for (int i=0 ; i<13 ; i++)
		{
			push = push.getRelative(event.getDirection());

			if (push.getType() == Material.AIR)
				return;

			if (Contains(push))
			{
				push.getWorld().playEffect(push.getLocation(), Effect.STEP_SOUND, push.getTypeId());
				event.setCancelled(true);
				return;	
			}		
		}			
	}

	@EventHandler
	public void ExpireBlocks(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		ArrayList<Block> toRemove = new ArrayList<Block>();

		for (BlockRestoreData cur : _blocks.values())
			if (cur.expire()) 
				toRemove.add(cur._block);

		//Remove Handled
		for (Block cur : toRemove)
			_blocks.remove(cur);
	}

	public void Restore(Block block)
	{
		if (!Contains(block))
			return;

		_blocks.remove(block).restore();
	}
	
	public void RestoreAll()
	{
		for (BlockRestoreData data : _blocks.values())
			data.restore();
		
		_blocks.clear();
	}
	
	public HashSet<Location> RestoreBlockAround(Material type, Location location, int radius)
	{
		HashSet<Location> restored = new HashSet<Location>();
		
		Iterator<Block> blockIterator = _blocks.keySet().iterator();
		
		while (blockIterator.hasNext())
		{
			Block block = blockIterator.next();
			
			if (block.getType() != type)
				continue;
			
			if (UtilMath.offset(block.getLocation().add(0.5, 0.5, 0.5), location) > radius)
				continue;
			
			restored.add(block.getLocation().add(0.5, 0.5, 0.5));
			
			_blocks.get(block).restore();
			
			blockIterator.remove();
		}
		
		return restored;
	}

	
	public void Add(Block block, int toID, byte toData, long expireTime)
	{
		Add(block, toID, toData, block.getTypeId(), block.getData(), expireTime);
	}
	
	public void Add(Block block, int toID, byte toData, int fromID, byte fromData, long expireTime)
	{ 
		if (!Contains(block))		GetBlocks().put(block, new BlockRestoreData(block, toID, toData, fromID, fromData, expireTime, 0));
		else						GetData(block).update(toID, toData, expireTime);
	}

	public void Snow(Block block, byte heightAdd, byte heightMax, long expireTime, long meltDelay, int heightJumps)
	{
		//Fill Above
		if (((block.getTypeId() == 78 && block.getData() >= (byte)7) || block.getTypeId() == 80) && GetData(block) != null)
		{
			GetData(block).update(78, heightAdd, expireTime, meltDelay);

			if (heightJumps > 0)	Snow(block.getRelative(BlockFace.UP), heightAdd, heightMax, expireTime, meltDelay, heightJumps-1);
			if (heightJumps == -1)	Snow(block.getRelative(BlockFace.UP), heightAdd, heightMax, expireTime, meltDelay, -1);

			return;
		}

		//Not Grounded
		if (!UtilBlock.solid(block.getRelative(BlockFace.DOWN)) && block.getRelative(BlockFace.DOWN).getTypeId() != 78)
			return;	

		//Not on Solid Snow
		if (block.getRelative(BlockFace.DOWN).getTypeId() == 78 && block.getRelative(BlockFace.DOWN).getData() < (byte)7)
			return;

		//No Snow on Ice
		if (block.getRelative(BlockFace.DOWN).getTypeId() == 79 || block.getRelative(BlockFace.DOWN).getTypeId() == 174)
			return;

		//No Snow on Slabs
		if (block.getRelative(BlockFace.DOWN).getTypeId() == 44 || block.getRelative(BlockFace.DOWN).getTypeId() == 126)
			return;

		//No Snow on Stairs
		if (block.getRelative(BlockFace.DOWN).getType().toString().contains("STAIRS"))
			return;

		//No Snow on Fence or Walls
		if (block.getRelative(BlockFace.DOWN).getType().name().toLowerCase().contains("fence") ||
			block.getRelative(BlockFace.DOWN).getType().name().toLowerCase().contains("wall"))
			return;

		//Not Buildable
		if (!UtilBlock.airFoliage(block) && block.getTypeId() != 78 && block.getType() != Material.CARPET)
			return;

		//Limit Build Height
		if (block.getTypeId() == 78)
			if (block.getData() >= (byte)(heightMax-1))
				heightAdd = 0;

		//Snow
		if (!Contains(block))		
			GetBlocks().put(block, new BlockRestoreData(block, 78, (byte)Math.max(0, heightAdd-1), block.getTypeId(), block.getData(), expireTime, meltDelay));
		else						
			GetData(block).update(78, heightAdd, expireTime, meltDelay);
	}

	public boolean Contains(Block block)
	{
		if (GetBlocks().containsKey(block))
			return true;
		return false;
	}

	public BlockRestoreData GetData(Block block)
	{
		if (_blocks.containsKey(block))
			return _blocks.get(block);
		return null;
	}

	public HashMap<Block, BlockRestoreData> GetBlocks()
	{
		return _blocks;
	}

	
}
