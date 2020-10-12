package ehnetwork.core.resourcepack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.core.resourcepack.redis.RedisUnloadResPack;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;
import ehnetwork.serverdata.commands.ServerCommandManager;

public class ResPackManager implements CommandCallback
{
	private ResUnloadCheck _packUnloadCheck;

	public ResPackManager(ResUnloadCheck packUnloadCheck)
	{
		_packUnloadCheck = packUnloadCheck;

		ServerCommandManager.getInstance().registerCommandType("RedisUnloadResPack", RedisUnloadResPack.class, this);
	}

	@Override
	public void run(ServerCommand command)
	{
		if (command instanceof RedisUnloadResPack)
		{
			RedisUnloadResPack redisCommand = (RedisUnloadResPack) command;

			Player player = Bukkit.getPlayerExact(redisCommand.getPlayer());

			if (player != null)
			{
				if (_packUnloadCheck.canSendUnload(player))
				{
					player.setResourcePack("http://www.chivebox.com/file/c/empty.zip");
				}
			}
		}
	}

}
