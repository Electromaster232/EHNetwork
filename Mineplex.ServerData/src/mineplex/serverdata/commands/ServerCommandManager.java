package mineplex.serverdata.commands;

import java.util.HashMap;
import java.util.Map;

import mineplex.serverdata.Utility;
import mineplex.serverdata.servers.ServerManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ServerCommandManager 
{

	// The singleton instance of ServerCommandManager
	private static ServerCommandManager _instance;
	
	public final String SERVER_COMMANDS_CHANNEL = "commands.server";
	
	private JedisPool _writePool;
	private JedisPool _readPool;
	private Map<String, CommandType> _commandTypes;
	
	private String _localServerName;
	public void initializeServer(String serverName) { _localServerName = serverName; }
	public boolean isServerInitialized() { return _localServerName != null; }
	
	/**
	 * Private class constructor to prevent non-singleton instances.
	 */
	private ServerCommandManager()
	{
		_writePool = Utility.generatePool(ServerManager.getMasterConnection());	// Publish to master instance
		_readPool = Utility.generatePool(ServerManager.getSlaveConnection());	// Read from slave instance
		
		_commandTypes = new HashMap<String, CommandType>();
		
		initialize();
	}
	
	/**
	 * Initialize the ServerCommandManager by subscribing to the
	 * redis network.
	 */
	private void initialize()
	{
		final Jedis jedis = _readPool.getResource();
		
		// Spin up a new thread and subscribe to the Redis pubsub network
		Thread thread = new Thread("Redis Manager")
		{
			public void run()
			{
				try
				{
					jedis.psubscribe(new ServerCommandListener(), SERVER_COMMANDS_CHANNEL + ":*");
				}
				catch (JedisConnectionException exception)
				{
					exception.printStackTrace();
					_readPool.returnBrokenResource(jedis);
				}
				finally
				{
					if (_readPool != null)
					{
						_readPool.returnResource(jedis);
					}
				}
			}
		};
		
		thread.start();
	}
	
	/**
	 * Publish a {@link ServerCommand} across the network to all live servers.
	 * @param serverCommand - the {@link ServerCommand} to issue to all servers.
	 */
	public void publishCommand(final ServerCommand serverCommand)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				Jedis jedis = _writePool.getResource();
				
				try
				{
					String commandType = serverCommand.getClass().getSimpleName();
					String serializedCommand = Utility.serialize(serverCommand);
					jedis.publish(SERVER_COMMANDS_CHANNEL + ":" + commandType, serializedCommand);
				}
				catch (JedisConnectionException exception)
				{
					exception.printStackTrace();
					_writePool.returnBrokenResource(jedis);
					jedis = null;
				}
				finally
				{
					if (_writePool != null)
					{
						_writePool.returnResource(jedis);
					}
				}
			}
		}).start();
	}
	
	/**
	 * Handle an incoming (serialized) {@link ServerCommand}.
	 * @param commandType - the type of command being received
	 * @param serializedCommand - the serialized {@link ServerCommand} data.
	 */
	public void handleCommand(final String commandType, String serializedCommand)
	{
		if (!isServerInitialized())
		{
			// TODO: Log un-initialized server receiving command?
			return;
		}
		
		if (_commandTypes.containsKey(commandType))
		{
			Class<? extends ServerCommand> commandClazz = _commandTypes.get(commandType).getCommandType();
			final ServerCommand serverCommand = Utility.deserialize(serializedCommand, commandClazz);

			if (serverCommand.isTargetServer(_localServerName))
			{
				// TODO: Run synchronously?
				CommandCallback callback = _commandTypes.get(commandType).getCallback();
				serverCommand.run(); // Run server command without callback
	
				if (callback != null)
				{
					callback.run(serverCommand); // Run callback
				}
			}
		}
	}
	
	/**
	 * Register a new type of {@link ServerCommand}.
	 * @param commandType - the {@link ServerCommand} type to register.
	 */
	public void registerCommandType(String commandName, Class<? extends ServerCommand> commandType, CommandCallback callback)
	{
		if (_commandTypes.containsKey(commandName))
		{
			// Log overwriting of command type?
		}
		
		CommandType cmdType = new CommandType(commandType, callback);
		_commandTypes.put(commandName, cmdType);
		System.out.println("Registered : " + commandName);
	}
	
	public void registerCommandType(String commandName, Class<? extends ServerCommand> commandType)
	{
		registerCommandType(commandName, commandType, null);
	}
	
	/**
	 * @return the singleton instance of ServerCommandManager
	 */
	public static ServerCommandManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ServerCommandManager();
		}
		
		return _instance;
	}
}
