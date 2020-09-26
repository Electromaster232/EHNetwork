package ehnetwork.core.teleport.redis;

import java.util.UUID;

import ehnetwork.serverdata.commands.ServerCommand;

public class RedisLocate extends ServerCommand
{
	private String _sender;
	private String _sendingServer;
	private String _target;
	private UUID _uuid = UUID.randomUUID();

	public RedisLocate(String sendingServer, String sender, String target)
	{
		_sender = sender;
		_target = target;
		_sendingServer = sendingServer;
	}

	public String getSender()
	{
		return _sender;
	}

	public String getServer()
	{
		return _sendingServer;
	}

	public String getTarget()
	{
		return _target;
	}

	public UUID getUUID()
	{
		return _uuid;
	}
}
