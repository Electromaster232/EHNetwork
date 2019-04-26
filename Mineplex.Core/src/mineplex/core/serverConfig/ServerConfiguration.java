package mineplex.core.serverConfig;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.PlayerList;

import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.serverdata.Region;
import mineplex.serverdata.data.ServerGroup;
import mineplex.serverdata.servers.ServerManager;

public class ServerConfiguration extends MiniPlugin
{
	private CoreClientManager _clientManager;
	
	private Field _playerListMaxPlayers;
	private ServerGroup _serverGroup;

	public ServerConfiguration(JavaPlugin plugin, CoreClientManager clientManager)
	{
		super("Server Configuration", plugin);
		
		_clientManager = clientManager;
		Region region = plugin.getConfig().getBoolean("serverstatus.us") ? Region.US : Region.EU;
		String groupName = plugin.getConfig().getString("serverstatus.group");

		_serverGroup = ServerManager.getServerRepository(region).getServerGroup(groupName);
		
		if (_serverGroup == null)
			return;
		
		try
		{
			_playerListMaxPlayers = PlayerList.class.getDeclaredField("maxPlayers");
			_playerListMaxPlayers.setAccessible(true);
			_playerListMaxPlayers.setInt(((CraftServer)_plugin.getServer()).getHandle(), _serverGroup.getMaxPlayers());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		_plugin.getServer().setWhitelist(_serverGroup.getWhitelist());
		((CraftServer)_plugin.getServer()).getServer().setPvP(_serverGroup.getPvp());
		//((CraftServer)_plugin.getServer()).getServer().setTexturePack(_serverGroup.getResourcePack());
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		if (_serverGroup.getStaffOnly() && !_clientManager.Get(event.getPlayer().getName()).GetRank().Has(event.getPlayer(), Rank.HELPER, false))
			event.disallow(Result.KICK_OTHER, "This is a staff only server.");
	}
	
	public ServerGroup getServerGroup()
	{
		return _serverGroup;
	}
}
