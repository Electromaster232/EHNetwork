package mineplex.core.stats;

import java.util.Set;

import mineplex.core.common.util.NautHashMap;

public class PlayerStats 
{
	private NautHashMap<String, Long> _statHash = new NautHashMap<String,Long>();
	
	public long addStat(String statName, long value)
	{
		if (!_statHash.containsKey(statName))
		{
			_statHash.put(statName, 0L);
		}
		
		_statHash.put(statName, _statHash.get(statName) + value);
		
		return _statHash.get(statName);
	}

	public long getStat(String statName) 
	{
		return _statHash.containsKey(statName) ? _statHash.get(statName) : 0L;
	}

	public Set<String> getStatsNames() 
	{	
		return _statHash.keySet();
	}
}
