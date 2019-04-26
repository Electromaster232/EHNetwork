package mineplex.core.treasure.animation;

import mineplex.core.treasure.Treasure;

/**
 * Created by Shaun on 8/29/2014.
 */
public abstract class Animation
{
	private Treasure _treasure;
	private boolean _running;
	private int _ticks;

	public Animation(Treasure treasure)
	{
		_treasure = treasure;
		_running = true;
	}

	public void run()
	{
		tick();
		_ticks++;
	}

	protected abstract void tick();

	protected abstract void onFinish();

	public void finish()
	{
		if (_running)
		{
			_running = false;
			onFinish();
		}
	}

	public boolean isRunning()
	{
		return _running;
	}

	public int getTicks()
	{
		return _ticks;
	}

	public Treasure getTreasure()
	{
		return _treasure;
	}

}
