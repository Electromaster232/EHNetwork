package ehnetwork.game.microgames.game.games.survivalgames;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class ChunkChange 
{
	public Chunk Chunk;
	public long Time;
	public ArrayList<BlockChange> Changes;
	public short[] DirtyBlocks = new short[64];
	public short DirtyCount = 0;

	public ChunkChange(Location loc, int id, byte data)
	{
		Chunk = loc.getChunk();

		Changes = new ArrayList<BlockChange>();
		
		AddChange(loc, id, data);

		Time = System.currentTimeMillis();
	}
	
	public void AddChange(Location loc, int id, byte data)
	{
		Changes.add(new BlockChange(loc, id, data));
		
	    if (DirtyCount < 63) 
	    {
			short short1 = (short)((loc.getBlockX() & 0xF) << 12 | (loc.getBlockZ() & 0xF) << 8 | loc.getBlockY());
			
			for (int l = 0; l < DirtyCount; l++) 
			{
				if (DirtyBlocks[l] == short1) 
				{
					return;
				}
			}
			
			DirtyBlocks[(DirtyCount++)] = short1;
	    }
	    else
	    {
	    	DirtyCount++;
	    }
	}
}
