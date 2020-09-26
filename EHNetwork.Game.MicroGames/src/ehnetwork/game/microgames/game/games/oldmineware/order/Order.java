package ehnetwork.game.microgames.game.games.oldmineware.order;

import java.util.HashSet;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;

public abstract class Order implements Listener
{
	public OldMineWare Host;
	
	private String _order;
	
	private long _startTime;
	private long _duration;
	
	private HashSet<Player> _completed = new HashSet<Player>();
	
	public Order(OldMineWare host, String order)
	{
		Host = host;
		
		_order = order;
	}
	
	public void StartOrder(int stage)
	{
		_completed.clear();
		
		_startTime = System.currentTimeMillis();
		
		_duration = 60000;
		
		SubInitialize();
		Initialize();
	}
	
	public void SubInitialize()
	{
		
	}

	public void EndOrder()
	{
		Host.BlockBreakAllow.clear();
		Host.BlockPlaceAllow.clear();
		Host.ItemDropAllow.clear();
		Host.ItemPickupAllow.clear();
		Uninitialize();
	}
	
	public abstract void Initialize();
	public abstract void Uninitialize();
	
	public String GetOrder()
	{
		return _order;
	}
	
	public boolean Finish()
	{
		if (GetRemainingPlaces() <= 0)
			return true;
		
		return UtilTime.elapsed(_startTime, _duration);
	}
	
	public int GetTimeLeft() 
	{
		return (int)((_duration - (System.currentTimeMillis() - _startTime))/1000);
	}
	
	public void SetCompleted(Player player)
	{
		if (_completed.contains(player))
			return;
		
		_completed.add(player);
		UtilPlayer.message(player, C.cGreen + C.Bold + "You completed the task!");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);
	}
	
	public boolean IsCompleted(Player player)
	{
		return _completed.contains(player);
	}
	
	public abstract void FailItems(Player player);

	public float GetTimeLeftPercent() 
	{
		float a = (float)(_duration - (System.currentTimeMillis() - _startTime));
		float b = (float)(_duration);
		return a/b;
	}

	public int GetRemainingPlaces() 
	{
		return (int) Math.max(0, (Host.GetPlayers(true).size()*0.5) - _completed.size());
	}

	public boolean PlayerHasCompleted()
	{
		return !_completed.isEmpty();
	}
}
