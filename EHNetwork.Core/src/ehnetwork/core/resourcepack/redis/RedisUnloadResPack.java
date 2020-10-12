package ehnetwork.core.resourcepack.redis;

import ehnetwork.serverdata.commands.ServerCommand;

public class RedisUnloadResPack extends ServerCommand
{
	private String _player;

	public RedisUnloadResPack(String player)
	{

		_player = player;
	}

	public String getPlayer()
	{
		return _player;
	}
}
