package ehnetwork.core.simpleStats;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class SimpleStats extends MiniPlugin
{	
	private static Object _transferLock = new Object();
	
	private SimpleStatsRepository _repository = new SimpleStatsRepository();
	private NautHashMap<String, String> _entries = new NautHashMap<String, String>();
	
	public SimpleStats(JavaPlugin plugin)
	{
		super("SimpleStats", plugin);
		
		_repository.initialize();
	}

	/*
	public NautHashMap<String, String> getEntries()
	{
		synchronized (_transferLock)
		{
			return _entries;
		}
	}*/
	
	@EventHandler
	public void storeStatsUpdate(final UpdateEvent updateEvent)
	{				
		if (updateEvent.getType() != UpdateType.SLOW)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{	
				synchronized (_transferLock)
				{
					_entries = (_repository.retrieveStatRecords());
				}
			}
		});
	}
	
	public void store(String statName, String statValue)
	{
		final String statNameFinal = statName;
		final String statValueFinal = statValue;		
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				synchronized (_transferLock)
				{
					_repository.storeStatValue(statNameFinal, statValueFinal);
				}
			}
		});
	}
	
	public NautHashMap<String, String> getStat(String statName)
	{
		final String statNameFinal = statName;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				synchronized (_transferLock)
				{
					_entries = _repository.retrieveStat(statNameFinal);
				}
			}
		});
			
		return _entries;
	}
}
