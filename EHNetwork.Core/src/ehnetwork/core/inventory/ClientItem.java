package ehnetwork.core.inventory;

import ehnetwork.core.inventory.data.Item;

public class ClientItem
{
	public ehnetwork.core.inventory.data.Item Item;
	public int Count;
	
	public ClientItem(Item item, int count)
	{
		Item = item;
		Count = count;
	}
}
