package ehnetwork.core.status;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.core.portal.Portal;
import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;
import ehnetwork.serverdata.commands.SuicideCommand;

public class SuicideHandler implements CommandCallback
{
	private ServerStatusManager _statusManager;
	private String _serverName;
	private Region _region;
	
	public SuicideHandler(ServerStatusManager statusManager, String serverName, Region region)
	{
		_statusManager = statusManager;
		_serverName = serverName;
		_region = region;
	}
	
	public void run(ServerCommand command)
	{
		if (command instanceof SuicideCommand)
		{
			String serverName = ((SuicideCommand)command).getServerName();
			Region region = ((SuicideCommand)command).getRegion();
			
			if (!serverName.equalsIgnoreCase(_serverName) || _region != region)
				return;
			
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage(F.main("Cleanup", "Server is being cleaned up, you're being sent to a lobby."));
			}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
			{
				public void run()
				{
					Portal.getInstance().sendAllPlayers("Lobby");
				}
			}, 60L);
			
			_statusManager.disableStatus();
		}
	}
}
