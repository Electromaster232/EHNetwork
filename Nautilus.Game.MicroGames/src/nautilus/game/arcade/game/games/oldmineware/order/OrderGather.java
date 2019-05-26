package nautilus.game.arcade.game.games.oldmineware.order;

import java.util.HashMap;

import mineplex.core.common.util.UtilInv;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.game.games.oldmineware.OldMineWare;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

public abstract class OrderGather extends Order
{
	private HashMap<Player, Integer> _counter = new HashMap<Player, Integer>();

	private int _id;
	private byte _data;
	private int _req;
	
	public OrderGather(OldMineWare host, String order, int id, int data, int required) 
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
		Host.BlockBreakAllow.add(_id);
		Host.ItemPickupAllow.add(_id);
	}
	
	@Override
	public void FailItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(_id, _data, _req));
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (!UtilInv.IsItem(event.getItem().getItemStack(), null, _id, _data))
			return;
		
		if (Has(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
		
		Add(event.getPlayer(), event.getItem().getItemStack().getAmount());
		
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
