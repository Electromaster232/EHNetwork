package mineplex.core.teleport.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mineplex.core.teleport.Teleport;
import mineplex.serverdata.commands.CommandCallback;
import mineplex.serverdata.commands.ServerCommand;

public class RedisLocateHandler implements CommandCallback
{
	private Teleport _plugin;
	private String _serverName;

	public RedisLocateHandler(Teleport plugin)
	{
		_plugin = plugin;
		_serverName = _plugin.getPlugin().getConfig().getString("serverstatus.name");
	}

	@Override
	public void run(ServerCommand command)
	{
		if (command instanceof RedisLocate)
		{
			RedisLocate locate = (RedisLocate) command;

			Player target = Bukkit.getPlayerExact(locate.getTarget());

			if (target != null)
			{
				RedisLocateCallback callback = new RedisLocateCallback(locate, _serverName, target.getName());
				callback.publish();
			}
		}
		else if (command instanceof RedisLocateCallback)
		{
			_plugin.handleLocateCallback((RedisLocateCallback) command);
		}
	}

}
