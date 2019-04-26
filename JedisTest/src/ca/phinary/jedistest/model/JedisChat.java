package ca.phinary.jedistest.model;

import ca.phinary.jedistest.api.Messenger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisChat
{
	private JedisPool _jedisPool;
	private String _channel;

	private Messenger _messenger;
	private JedisPublisher _publisher;

	public JedisChat(Messenger messenger, final String channel, String host, int port)
	{
		_jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
		_channel = channel;
		_messenger = messenger;

		_publisher = new JedisPublisher(messenger.getConsole(), _jedisPool, channel);
		_messenger.getChat().addListener(_publisher);

		startListen();
	}

	public void startListen()
	{
		_messenger.getConsole().println("Attempting to connect to redis server...");
		try
		{
			final Jedis jedis = _jedisPool.getResource();

			_messenger.getConsole().println("Successfully connected!");

			SubscribeWorker worker = new SubscribeWorker(_messenger.getConsole(), _jedisPool, _channel);
			worker.execute();

		} catch (Exception e)
		{
			_messenger.getConsole().println("Failed to connect to redis server!");
		}
	}

	public void close()
	{
		System.out.println("close");
		_publisher.close();
	}
}
