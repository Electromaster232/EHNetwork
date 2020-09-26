package ehnetwork.game.arcade.game.games.oldmineware.order;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.game.games.oldmineware.OldMineWare;

public abstract class OrderCraft extends Order
{
	private HashMap<Player, Integer> _counter = new HashMap<Player, Integer>();

	private int _id;
	private byte _data;
	private int _req;
	
	public OrderCraft(OldMineWare host, String order, int id, int data, int required) 
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
	}
	
	@Override
	public void FailItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(_id, _data, _req));
	}
	
	@EventHandler
	public void Craft(InventoryClickEvent event)
	{
		if (event.getSlotType() != SlotType.RESULT)
			return;
		
		if (!UtilInv.IsItem(event.getCurrentItem(), null, _id, _data))
			return;
		
		if (!(event.getWhoClicked() instanceof Player))
			return;
		
		Player player = (Player)event.getWhoClicked();
		
		if (!event.isShiftClick())
		{
			Add(player, event.getCurrentItem().getAmount());
		}
		else
		{
			CraftingInventory inv = (CraftingInventory)event.getInventory();

			int make = 128;

			//Find Lowest Amount
			for (ItemStack item : inv.getMatrix())
				if (item != null && item.getType() != Material.AIR)
					if (item.getAmount() < make)
						make = item.getAmount();

			make = make * event.getCurrentItem().getAmount();
			
			Add(player, make);
		}
		
		if (Has(player))
			SetCompleted(player);
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
