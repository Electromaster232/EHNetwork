package ehnetwork.bungee.playerTracker;

import java.io.File;

import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.data.DataRepository;
import ehnetwork.serverdata.data.PlayerStatus;
import ehnetwork.serverdata.redis.RedisDataRepository;
import ehnetwork.serverdata.servers.ServerManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class PlayerTracker implements Listener
{
	// Default period before status expiry (8 hours)
	private static final int DEFAULT_STATUS_TIMEOUT = 60 * 60 * 8;

	// Repository storing player status' across network.
	private DataRepository<PlayerStatus> _repository;
	
	private Plugin _plugin;
	
	public PlayerTracker(Plugin plugin)
	{
		_plugin = plugin;

		_plugin.getProxy().getPluginManager().registerListener(_plugin, this);
		
		Region region = !new File("eu.dat").exists() ? Region.US : Region.EU;
		_repository = new RedisDataRepository<PlayerStatus>(ServerManager.getMasterConnection(), ServerManager.getSlaveConnection(),
				region, PlayerStatus.class, "playerStatus");
		
		System.out.println("Initialized PlayerTracker.");
	}

	@EventHandler
	public void playerConnect(final ServerConnectedEvent event)
	{
		_plugin.getProxy().getScheduler().runAsync(_plugin, new Runnable()
		{
			public void run()
			{
				PlayerStatus snapshot = new PlayerStatus(event.getPlayer().getName(), event.getServer().getInfo().getName());
				_repository.addElement(snapshot, DEFAULT_STATUS_TIMEOUT);
			}
		});
	}

	@EventHandler
	public void playerDisconnect(final PlayerDisconnectEvent event)
	{
		_plugin.getProxy().getScheduler().runAsync(_plugin, new Runnable()
		{
			public void run()
			{
				_repository.removeElement(event.getPlayer().getName());
			}
		});
	}
}
