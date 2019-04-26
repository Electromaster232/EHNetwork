package mineplex.core.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

public class Region
{
	private String _name;
	private transient Vector _pointOne;
	private transient Vector _pointTwo;
	
	private List<String> _owners;
	
	private Boolean _blockPassage = false;
	private Boolean _blockChange = true;
	
	private int _priority;
	
	private int _minX;
	private int _minY;
	private int _minZ;
	
	private int _maxX;
	private int _maxY;
	private int _maxZ;
	
	public Region(String name, Vector pointOne, Vector pointTwo)
	{
		_name = name;
		_pointOne = pointOne;
		_pointTwo = pointTwo;
		_priority = 0;
		_owners = new ArrayList<String>();

		UpdateMinMax();
	}
	
	public Vector GetMaximumPoint()
	{
		return new Vector(_maxX, _maxY, _maxZ);
	}
	
	public void AdjustRegion(Vector vector)
	{
		_minX += vector.getBlockX();
		_minY += vector.getBlockY();
		_minZ += vector.getBlockZ();
		
		_maxX += vector.getBlockX();
		_maxY += vector.getBlockY();
		_maxZ += vector.getBlockZ();
	}
	
	public Vector GetMinimumPoint()
	{
		return new Vector(_minX, _minY, _minZ);
	}
	
	public Vector GetMidPoint()
	{
		return new Vector((_maxX - _minX)/2 + _minX, (_maxY - _minY)/2  + _minY, (_maxZ - _minZ)/2  + _minZ);
	}
	
	public void SetPriority(int priority)
	{
		_priority = priority;
	}
	
	public int GetPriority()
	{
		return _priority;
	}
	
	public Boolean Contains(Vector v)
	{		
		return v.getBlockX() >= _minX && v.getBlockX() <= _maxX
			&& v.getBlockY() >= _minY && v.getBlockY() <= _maxY 
			&& v.getBlockZ() >= _minZ && v.getBlockZ() <= _maxZ;
	}
	
	public void AddOwner(String name)
	{
		if (!_owners.contains(name.toLowerCase()))
		{
			_owners.add(name.toLowerCase());
		}
	}
	
	public void RemoveOwner(String name)
	{
		_owners.remove(name.toLowerCase());
	}
	
	public void SetOwners(List<String> owners)
	{
		_owners = owners;
		
		for (String ownerName : _owners)
		{
			ownerName = ownerName.toLowerCase();
		}
	}
	
	public void SetEnter(Boolean canEnter)
	{
		_blockPassage = !canEnter;
	}
	
	public void SetChangeBlocks(Boolean canChangeBlocks)
	{
		_blockChange = !canChangeBlocks;
	}
	
	public Boolean CanEnter(String playerName)
	{
		if (_blockPassage)
		{
			if (!_owners.contains(playerName.toLowerCase()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public Boolean CanChangeBlocks(String playerName)
	{
		if (_blockChange)
		{
			if (!_owners.contains(playerName.toLowerCase()))
			{			
				return false;
			}
		}
		
		return true;
	}
	
	public String GetName()
	{
		return _name;
	}
	
	private void UpdateMinMax()
	{
		_minX = Math.min(_pointOne.getBlockX(), _pointTwo.getBlockX());
		_minY = Math.min(_pointOne.getBlockY(), _pointTwo.getBlockY());
		_minZ = Math.min(_pointOne.getBlockZ(), _pointTwo.getBlockZ());
		
		_maxX = Math.max(_pointOne.getBlockX(), _pointTwo.getBlockX());
		_maxY = Math.max(_pointOne.getBlockY(), _pointTwo.getBlockY());
		_maxZ = Math.max(_pointOne.getBlockZ(), _pointTwo.getBlockZ());
	}
	
	@Override
	public String toString()
	{
	    return "Maximum point: " + GetMaximumPoint() + " Minimum point: " + GetMinimumPoint();
	}
}
