package ca.phinary.jedistest.model;

import redis.clients.jedis.JedisPubSub;

public class JedisSubscriber extends JedisPubSub
{
	private SubscribeWorker _jedisWorker;

	public JedisSubscriber(SubscribeWorker jedisWorker)
	{
		_jedisWorker = jedisWorker;
	}

	@Override
	public void onMessage(String channel, String message)
	{
		_jedisWorker.onMessage(message);
	}

	@Override
	public void onPMessage(String s, String s1, String s2)
	{
		System.out.println("Pmessage:" + s  + " " + s1 + " " + s2);
	}

	@Override
	public void onSubscribe(String s, int i)
	{
		System.out.println("Subcribe: s " + i);
	}

	@Override
	public void onUnsubscribe(String s, int i)
	{
		System.out.println("UnSubcribe: s " + i);
	}

	@Override
	public void onPUnsubscribe(String s, int i)
	{
		System.out.println("PUnSubcribe: s " + i);
	}

	@Override
	public void onPSubscribe(String s, int i)
	{
		System.out.println("Subcribe: s " + i);
	}
}
