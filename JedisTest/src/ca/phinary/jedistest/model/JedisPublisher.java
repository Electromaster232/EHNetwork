package ca.phinary.jedistest.model;

import javax.swing.*;
import java.net.InetAddress;
import java.util.List;

import ca.phinary.jedistest.api.ChatListener;
import ca.phinary.jedistest.api.Console;
import redis.clients.jedis.JedisPool;

public class JedisPublisher implements ChatListener
{
	private Console _console;
	private JedisPool _jedisPool;
	private String _channelName;

	public JedisPublisher(Console console, JedisPool jedisPool, String channelName)
	{
		_console = console;
		_jedisPool = jedisPool;
		_channelName = channelName;

		sendConnectMessage();
	}

	@Override
	public void onChat(String message)
	{
		String hostName = "Unknown";

		try
		{
			hostName = InetAddress.getLocalHost().toString();
		}
		catch(Exception e) { };

		message(hostName + " > " + message);
	}

	private void sendConnectMessage()
	{
		String hostName = "Unknown";

		try
		{
			hostName = InetAddress.getLocalHost().toString();
		}
		catch(Exception e) { };

		message(hostName + " has connected to the channel");
	}

	public void close()
	{
		String hostName = "Unknown";

		try
		{
			hostName = InetAddress.getLocalHost().toString();
		}
		catch(Exception e) { };


		message(hostName + " has disconnected from the channel");
	}

	private void message(final String text)
	{
		new SwingWorker<Void, String>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				try
				{
					_jedisPool.getResource().publish(_channelName, text);
				}
				catch (Exception e)
				{
					publish("Failed to send message: " + e.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks)
			{
				for (String s : chunks)
					_console.println(s);
			}
		}.execute();
	}
}
