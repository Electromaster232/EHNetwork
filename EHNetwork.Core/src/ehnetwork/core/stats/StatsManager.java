package ehnetwork.core.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniDbClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.stats.command.GiveStatCommand;
import ehnetwork.core.stats.command.TimeCommand;
import ehnetwork.core.stats.event.StatChangeEvent;

public class StatsManager extends MiniDbClientPlugin<PlayerStats>
{
	private static Object _statSync = new Object();
	
	private StatsRepository _repository;
	
	private NautHashMap<String, Integer> _stats = new NautHashMap<String, Integer>();
	private NautHashMap<Player, NautHashMap<String, Long>> _statUploadQueue = new NautHashMap<Player, NautHashMap<String, Long>>();
	private Runnable _saveRunnable;
	
	public StatsManager(JavaPlugin plugin, CoreClientManager clientManager)
	{
		super("Stats Manager", plugin, clientManager);

		_repository = new StatsRepository(plugin);
		
		if (_saveRunnable == null)
		{
			_saveRunnable = new Runnable()
			{
				public void run()
				{
					saveStats();
				}
			};
			
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, _saveRunnable, 20L, 20L);
		}
		
		for (Stat stat : _repository.retrieveStats())
		{
			_stats.put(stat.Name, stat.Id);
		}
		
		clientManager.addStoredProcedureLoginProcessor(new SecondaryStatHandler(this, _repository));
	}
	
	public void incrementStat(final Player player, final String statName, final long value)
	{
		long newValue = Get(player).addStat(statName, value);

		//Event
		UtilServer.getServer().getPluginManager().callEvent(new StatChangeEvent(player.getName(), statName, newValue - value, newValue));
		
		// Verify stat is in our local cache, if not add it remotely.
		if (!_stats.containsKey(statName))
		{
			Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
			{
				public void run()
				{
					synchronized (_statSync)
					{
						// If many players come in for a new stat, when the first add finishes the others are queued to add again
						// This makes a second check for the stat name (already added before lock was released)
						// Then it pops into queue and forgets adding the new stat to db.
						if (_stats.containsKey(statName))
						{
							addToQueue(statName, player, value);
							return;
						}
						
						_repository.addStat(statName);
						
						_stats.clear();
						
						for (Stat stat : _repository.retrieveStats())
						{
							_stats.put(stat.Name, stat.Id);
						}
						
						addToQueue(statName, player, value);
					}
				}
			});
		}
		else
		{
			addToQueue(statName, player, value);
		}
	}
	
	private void addToQueue(String statName, Player player, long value)
	{
		synchronized (_statSync)
		{
			if (!_statUploadQueue.containsKey(player))
				_statUploadQueue.put(player, new NautHashMap<String, Long>());
			
			if (!_statUploadQueue.get(player).containsKey(statName))
				_statUploadQueue.get(player).put(statName, 0L);
			
			_statUploadQueue.get(player).put(statName, _statUploadQueue.get(player).get(statName) + value);
		}
	}

	protected void saveStats() 
	{
		if (_statUploadQueue.isEmpty())
			return;
		
		try
		{
			NautHashMap<Integer, NautHashMap<Integer, Long>> uploadQueue = new NautHashMap<Integer, NautHashMap<Integer, Long>>();
			
			synchronized (_statSync)
			{
				for (Iterator<Player> statIterator = _statUploadQueue.keySet().iterator(); statIterator.hasNext();)
				{
					Player player = statIterator.next();
					
					if (player.isOnline())
						continue;
					
					int uploadKey = ClientManager.getCachedClientAccountId(player.getUniqueId());
					
					uploadQueue.put(uploadKey, new NautHashMap<Integer, Long>());
					
					for (String statName : _statUploadQueue.get(player).keySet())
					{
						int statId = _stats.get(statName);
						uploadQueue.get(uploadKey).put(statId, _statUploadQueue.get(player).get(statName));
						System.out.println(player.getName() + " saving stat : " + statName + " adding " + _statUploadQueue.get(player).get(statName));
					}
					
					statIterator.remove();
				}
			}

			_repository.saveStats(uploadQueue);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public boolean incrementStat(final int accountId, final String statName, final long value)
	{
		if (_stats.containsKey(statName))
			return false;
		
		final NautHashMap<Integer, NautHashMap<Integer, Long>> uploadQueue = new NautHashMap<Integer, NautHashMap<Integer, Long>>();
		uploadQueue.put(accountId, new NautHashMap<Integer, Long>());
		uploadQueue.get(accountId).put(_stats.get(statName), value);
		
		runAsync(new Runnable()
		{
			public void run()
			{
				_repository.saveStats(uploadQueue);
			}
		});
		
		return true;
	}
	
	public int getStatId(String statName)
	{
		return _stats.get(statName);
	}
	
	public void replacePlayerHack(String playerName, PlayerStats playerStats)
	{
		Set(playerName, playerStats);
	}
	
	@Override
	protected PlayerStats AddPlayer(String player)
	{
		return new PlayerStats();
	}

	public PlayerStats getOfflinePlayerStats(String playerName) throws SQLException
	{
		return _repository.loadOfflinePlayerStats(playerName);
	}

	@Override
	public void addCommands()
	{
		addCommand(new TimeCommand(this));
		addCommand(new GiveStatCommand(this));
	}

	@Override
	public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
	{
		Set(playerName, _repository.loadClientInformation(resultSet));
	}

	@Override
	public String getQuery(int accountId, String uuid, String name)
	{
		return "SELECT stats.name, value FROM accountStats INNER JOIN stats ON stats.id = accountStats.statId WHERE accountStats.accountId = '" + accountId + "';";
	}
}
