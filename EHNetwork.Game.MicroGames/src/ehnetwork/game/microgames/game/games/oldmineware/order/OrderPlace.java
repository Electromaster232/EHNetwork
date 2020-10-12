package ehnetwork.game.microgames.game.games.oldmineware.order;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;

public abstract class OrderPlace extends Order
{
	private HashMap<Player, Integer> _counter = new HashMap<Player, Integer>();

	private int _id;
	private byte _data;
	private int _req;
	
	public OrderPlace(OldMineWare host, String order, int id, int data, int required) 
	{
		super(host, order);
		
		_id = id;
		_data = (byte)data;
		_req = required;
	}
	
	@Override
	public void SubInitialize()
	{
		_counter.clear();
		Host.BlockPlaceAllow.add(_id);
	}
	
	@Override
	public void FailItems(Player player) 
	{
	
	}
	
	@EventHandler
	public void Place(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getTypeId() != _id)
			return;
		
		if (_data != -1 && event.getBlock().getData() != _data)
			return;
		
		Add(event.getPlayer(), 1);
		
		if (Has(event.getPlayer()))
			SetCompleted(event.getPlayer());
	}
	
	public void Add(Player player, int add)
	{
		if (!_counter.containsKey(player))
			_counter.put(player, add);
		
		else
			_counter.put(player, _counter.get(player) + add);
		
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2f, 1.5f);
	}
	
	public boolean Has(Player player)
	{
		if (!_counter.containsKey(player))
			return false;
		
		return _counter.get(player) >= _req;
	}
}
