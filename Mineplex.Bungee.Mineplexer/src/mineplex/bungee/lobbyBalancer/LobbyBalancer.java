package mineplex.bungee.lobbyBalancer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mineplex.serverdata.Region;
import mineplex.serverdata.data.MinecraftServer;
import mineplex.serverdata.servers.ServerManager;
import mineplex.serverdata.servers.ServerRepository;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class LobbyBalancer implements Listener, Runnable
{
	private Plugin _plugin;
	private ServerRepository _repository;
	
	private List<MinecraftServer> _sortedLobbies = new ArrayList<MinecraftServer>();
	private static Object _serverLock = new Object();
	
	private int _lobbyIndex = 0;
	
	public LobbyBalancer(Plugin plugin)
	{
		_plugin = plugin;
		
		Region region = !new File("eu.dat").exists() ? Region.US : Region.EU;
		_repository = ServerManager.getServerRepository(region);
		
		loadLobbyServers();
		
		_plugin.getProxy().getPluginManager().registerListener(_plugin, this);
		_plugin.getProxy().getScheduler().schedule(_plugin, this, 250L, 250L, TimeUnit.MILLISECONDS);
	}
	
	@EventHandler
	public void playerConnect(ServerConnectEvent event)
	{
		if (!event.getTarget().getName().equalsIgnoreCase("Lobby"))
			return;

		synchronized (_serverLock)
		{
			if (_lobbyIndex >= _sortedLobbies.size() || _sortedLobbies.get(_lobbyIndex).getPlayerCount() >= _sortedLobbies.get(_lobbyIndex).getMaxPlayerCount())
				_lobbyIndex = 0;
			
			event.setTarget(_plugin.getProxy().getServerInfo(_sortedLobbies.get(_lobbyIndex).getName()));
			_sortedLobbies.get(_lobbyIndex).incrementPlayerCount(1);
			System.out.println("Sending " + event.getPlayer().getName() + " to " + _sortedLobbies.get(_lobbyIndex).getName() + "(" + _sortedLobbies.get(_lobbyIndex).getPublicAddress() + ")");
			_lobbyIndex++;
		}
	}
	
	public void run()
	{
		loadLobbyServers();
	}
    
	public void loadLobbyServers()
	{		
		Collection<MinecraftServer> servers = _repository.getServerStatuses();
			
		synchronized (_serverLock)
		{
			long startTime = System.currentTimeMillis();
			_sortedLobbies.clear();
			
			for (MinecraftServer server : servers)
			{
				if (server.getName() == null)
					continue;
				
				InetSocketAddress socketAddress = new InetSocketAddress(server.getPublicAddress(), server.getPort());
				_plugin.getProxy().getServers().put(server.getName(), _plugin.getProxy().constructServerInfo(server.getName(), socketAddress, "LobbyBalancer", false));
				
	    		if (server.getName().toUpperCase().contains("LOBBY"))
	    		{
	    			if (server.getMotd() == null || !server.getMotd().contains("Restarting"))
	    			{
	    				_sortedLobbies.add(server);
	    			}
	    		}
			}
			
			Collections.sort(_sortedLobbies, new LobbySorter());
			
            long timeSpentInLock = System.currentTimeMillis() - startTime;
            
            if (timeSpentInLock > 50)
            	System.out.println("[==] TIMING [==] Locked loading servers for " + timeSpentInLock + "ms");
		}
	}
}
