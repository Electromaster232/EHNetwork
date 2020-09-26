package ehnetwork.core.friend.ui;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import ehnetwork.core.common.util.NautHashMap;

public class TabList implements Listener
{
	private static int MAX_SLOTS = 64;
	private static int COLUMN_SLOTS = 16;
	
	private static NautHashMap<Integer, String> _invisibleHolders = new NautHashMap<Integer, String>();
	
	private NautHashMap<Integer, LineTracker> _tabSlots = new NautHashMap<Integer, LineTracker>();
	private HashSet<Integer> _updatedSlots = new HashSet<Integer>();
	
	private boolean _update;
	
	static {
		String spaces = "";
		
		for (int i=0; i < MAX_SLOTS; i++)
		{
			int markerSymbol = i / COLUMN_SLOTS;
			String symbol = null;
			
			if (i % COLUMN_SLOTS == 0)
				spaces = "";
			else
				spaces += " ";
			
			if (markerSymbol == 0)
			{
				symbol = ChatColor.GREEN + "";
			}
			else if (markerSymbol == 1)
			{
				symbol = ChatColor.RED + "";
			}
			else if (markerSymbol == 2)
			{
				symbol = ChatColor.BLUE + "";
			}
			else if (markerSymbol == 3)
			{
				symbol = ChatColor.BLACK + "";
			}
			
			_invisibleHolders.put(i, symbol + spaces);
		}
	}
	
	public TabList()
	{
		for (Integer i=0; i < MAX_SLOTS; i++)
		{
			_tabSlots.put(i, new LineTracker(_invisibleHolders.get(i)));
		}
	}
	
	public void set(int column, int row, String lineContent)
	{
		int index = row * 4 + column;
		
		if (index >= MAX_SLOTS)
			return;
		
		if (lineContent == null || lineContent.isEmpty())
			lineContent = _invisibleHolders.get(index);
		
		if (_tabSlots.get(index).setLine(lineContent))
		{
			_updatedSlots.add(index);
			_update = true;
		}
	}
	
	public void refreshForPlayer(Player player)
	{
		EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

		int indexChanged = MAX_SLOTS;
		
		for (int i=0; i < MAX_SLOTS; i++)
		{
			if (indexChanged == MAX_SLOTS && _updatedSlots.contains(i))
			{
				indexChanged = i;
			}
			else if (indexChanged != MAX_SLOTS && !_updatedSlots.contains(i))
			{
				_tabSlots.get(i).removeLineForPlayer(entityPlayer);
			}
		}
		
		for (int i=indexChanged; i < MAX_SLOTS; i++)
		{
			_tabSlots.get(i).displayLineToPlayer(entityPlayer);
		}
		
		_update = false;
		_updatedSlots.clear();
	}
	
	public boolean shouldUpdate()	
	{
		return _update;
	}
}
