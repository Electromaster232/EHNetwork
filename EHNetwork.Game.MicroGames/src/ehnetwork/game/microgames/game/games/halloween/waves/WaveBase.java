package ehnetwork.game.microgames.game.games.halloween.waves;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.game.microgames.game.games.halloween.HalloweenAudio;

public abstract class WaveBase 
{
	protected Halloween Host;
	
	protected String _name;
	protected HalloweenAudio _audio;
	
	protected long _start;
	protected long _duration;
	
	private int _tick = 0;
	
	protected ArrayList<Location> _spawns;
	
	public WaveBase(Halloween host, String name, long duration, ArrayList<Location> spawns, HalloweenAudio audio)
	{
		Host = host;
		
		_name = name;
		_audio = audio;
		
		_start = System.currentTimeMillis();
		_duration = duration;
		
		_spawns = spawns;
	}
	
	public Location GetSpawn()
	{
		return _spawns.get(UtilMath.r(_spawns.size()));
	}
	
	public boolean Update(int wave)
	{
		//End
		if (_tick > 0 && UtilTime.elapsed(_start, _duration) && CanEnd())
		{
			System.out.println("Wave " + wave + " has ended.");
			return true;
		}
		
		//Start
		if (_tick == 0)
			_start = System.currentTimeMillis();
			
		//Announce
		if (_tick == 0)
		{
			System.out.println("Wave " + wave + " has started.");
			Host.Announce(C.cRed + C.Bold + "Wave " + wave + ": " + C.cYellow + _name);	
			
			UtilTextMiddle.display(C.cYellow + "Wave " + wave, _name, 10, 100, 20);
			
			if (_audio != null)
			{
				Host.playSound(_audio);
			}
		}
			
		//Display
		for (Player player : UtilServer.getPlayers())
			player.setExp(Math.min(0.999f, (float)(_duration - (System.currentTimeMillis() - _start)) / (float)_duration));
		
		//Spawn Beacons
		if (_tick == 0)
			SpawnBeacons(_spawns);

		//Spawn
		Host.CreatureAllowOverride = true;	
		Spawn(_tick++);
		Host.CreatureAllowOverride = false;
		
		return false;
	}
	
	public void SpawnBeacons(ArrayList<Location> locs)
	{
		//Average Location
		Vector total = new Vector(0,0,0);
		for (Location loc : locs)
			total.add(loc.toVector());
		total.multiply((double)1/(double)locs.size());
		
		//Beacon
		Block block = total.toLocation(locs.get(0).getWorld()).getBlock().getRelative(BlockFace.DOWN);
		Host.Manager.GetBlockRestore().Add(block, 138, (byte)0, _duration);
		
		for (int x=-1 ; x<=1 ; x++)
			for (int z=-1 ; z<=1 ; z++)
				Host.Manager.GetBlockRestore().Add(block.getRelative(x, -1, z), 42, (byte)0, _duration);
		
		//Clear Laser
		while (block.getY() < 250)
		{
			block = block.getRelative(BlockFace.UP);
			if (block.getType() != Material.AIR)
				Host.Manager.GetBlockRestore().Add(block, 0, (byte)0, _duration);
		}
	}
	
	public boolean CanEnd() 
	{
		return true;
	}

	public abstract void Spawn(int tick);
}
