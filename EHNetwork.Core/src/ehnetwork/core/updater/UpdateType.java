package ehnetwork.core.updater;

import ehnetwork.core.common.util.UtilTime;

public enum UpdateType  
{
	MIN_64(3840000),
	MIN_32(1920000),
	MIN_16(960000),
	MIN_08(480000),
	MIN_04(240000),
	MIN_02(120000),
	MIN_01(60000),
	SLOWEST(32000),
	SLOWER(16000),
	SLOW(4000),
	TWOSEC(2000),
	SEC(1000),
	FAST(500),
	FASTER(250),
	FASTEST(125),
	TICK(49);

	private long _time;
	private long _last;
	private long _timeSpent;
	private long _timeCount;
	
	UpdateType(long time)
	{
		_time = time;
		_last = System.currentTimeMillis();
	}
	
	public boolean Elapsed()
	{
		if (UtilTime.elapsed(_last, _time))
		{
			_last = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}

	public void StartTime()
	{
		_timeCount = System.currentTimeMillis();
	}

	public void StopTime()
	{
		_timeSpent += System.currentTimeMillis() - _timeCount;
	}

	public void PrintAndResetTime()
	{
		System.out.println(this.name() + " in a second: " + _timeSpent);
		_timeSpent = 0;
	}
}
