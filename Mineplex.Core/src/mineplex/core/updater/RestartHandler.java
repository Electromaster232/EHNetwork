package mineplex.core.updater;

import mineplex.core.common.util.F;
import mineplex.core.portal.Portal;
import mineplex.serverdata.Region;
import mineplex.serverdata.commands.CommandCallback;
import mineplex.serverdata.commands.RestartCommand;
import mineplex.serverdata.commands.ServerCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RestartHandler implements CommandCallback, Listener
{
	private JavaPlugin _plugin;
	private String _serverName;
	private Region _region;
	
	private boolean _restarting;
	
	public RestartHandler(JavaPlugin plugin, String serverName, Region region)
	{
		_plugin = plugin;
		_serverName = serverName;
		_region = region;
		
		_plugin.getServer().getPluginManager().registerEvents(this, _plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void reflectMotd(ServerListPingEvent event)
	{
		if (_restarting)
			event.setMotd("Restarting soon");
	}
	
	public void run(ServerCommand command)
	{
		if (command instanceof RestartCommand)
		{
			String serverName = ((RestartCommand)command).getServerName();
			Region region = ((RestartCommand)command).getRegion();
			
			if (!serverName.equalsIgnoreCase(_serverName) || _region != region)
				return;
			
			_restarting = true;
			
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage(F.main("Restart", "Server is restarting, you're being sent to a lobby."));
			}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
			{
				public void run()
				{
					Portal.getInstance().sendAllPlayers("Lobby");
				}
			}, 60L);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
			{
				public void run()
				{
					Bukkit.getServer().shutdown();
				}
			}, 100L);
		}
	}
}
