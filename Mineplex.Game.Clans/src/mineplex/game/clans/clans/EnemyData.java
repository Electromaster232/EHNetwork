package mineplex.game.clans.clans;

import java.util.Date;

import mineplex.core.common.util.UtilTime;

public class EnemyData
{
	private String _enemy;
	private boolean _initiator;
	private int _score;
	private int _kills;
	private Date _timeFormed;

	public EnemyData(String enemy, boolean initiator, int score, int kills, Date timeFormed)
	{
		_enemy = enemy;
		_initiator = initiator;
		_score = score;
		_kills = kills;
		_timeFormed = timeFormed;
	}

	public String getEnemyName()
	{
		return _enemy;
	}

	public int getScore()
	{
		return _score;
	}

	public int getKills()
	{
		return _kills;
	}

	public void addScore(int add)
	{
		_score = Math.max(0, Math.min(_score + add, 40));
	}

	public void addKill()
	{
		_kills++;
	}

	public boolean isInitiator()
	{
		return _initiator;
	}

	public Date getTimeFormed()
	{
		return _timeFormed;
	}

	public String getRelativeTimeFormed()
	{
		long timeFormed = _timeFormed.getTime();
		long currentTime = System.currentTimeMillis();
		return UtilTime.convertString(System.currentTimeMillis() - timeFormed, 1, UtilTime.TimeUnit.FIT);
	}
}
