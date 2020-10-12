package ca.phinary.jedistest.model;

import javax.swing.*;

import java.util.List;

import ca.phinary.jedistest.api.Console;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubscribeWorker extends SwingWorker<Void, String>
{
	private Console _console;
	private JedisPool _jedisPool;
	private JedisSubscriber _jedisSubscriber;
	private String _channel;

	public SubscribeWorker(Console console, JedisPool jedisPool, String channel)
	{
		_console = console;
		_jedisPool = jedisPool;
		_jedisSubscriber = new JedisSubscriber(this);
		_channel = channel;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		publish("Attempting to connect to channel: " + _channel);
		try
		{
			Jedis j = _jedisPool.getResource();
			publish("Successfully connected to channel!");
			_jedisPool.getResource().subscribe(_jedisSubscriber, _channel);
		} catch (Exception e)
		{
			publish("Connection to channel failed:" + e.getMessage());
		}

		return null;
	}

	public void onMessage(String s)
	{
		publish(s);
	}

	@Override
	protected void process(List<String> chunks)
	{
		for (String s : chunks)
		{
			_console.println(s);
		}
	}
}
