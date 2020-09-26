package ehnetwork.hub.modules.parkour;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;

public class SnakePart 
{
	private ArrayList<Location> _path = new ArrayList<Location>();
	private byte _color = 0;

	private int _index = 0;
	private boolean _colorTick = false;
	
	private int _pathId = 39;

	public SnakePart(ArrayList<Location> path)
	{
		_path = path;
		
		_color = path.get(1).getBlock().getData();
		
		for (Location loc : path)
			MapUtil.QuickChangeBlockAt(loc, 35, _color);
	}

	public void Update()
	{
		if (_path.isEmpty())
			return;

		//Set Block
		sendBlock(_path.get(_index), 35, GetColor());

		int back = _index - 10;
		while (back < 0)
			back += _path.size();
		
		//Unset Tail
		sendBlock(_path.get(back), _pathId, (byte) 0);

		//ALT
		if (_path.size() > 50)
		{
			int newIndex = (_index + (_path.size()/2))%_path.size();
			
			//Set Block
			sendBlock(_path.get(newIndex), 35, GetColor());

			back = newIndex - 10;
			if (back < 0)
				back += _path.size();

			//Unset Tail
			sendBlock(_path.get(back), _pathId, (byte) 0);
		}
		
		_index = (_index+1)%_path.size();
		_colorTick = !_colorTick;
		
		/*
		for (Location loc : _path)
			if (loc.getBlock().getType() == Material.AIR)
				UtilParticle.PlayParticle(ParticleType.CRIT, loc, 0, 0, 0, 0, 1);
				*/
	}
	
	public void sendBlock(Location loc, int id, byte data)
	{
		MapUtil.ChunkBlockChange(loc, id, data, false);
		
		
		for (Player player : UtilServer.getPlayers())
		{
			if (UtilMath.offset(player.getLocation(), loc) < 64)
			{
				player.sendBlockChange(loc, Material.getMaterial(id), data);
			}
		}
	}

	public byte GetColor()
	{
		if (_colorTick)	
			return _color;
		return _color;
	}
}
