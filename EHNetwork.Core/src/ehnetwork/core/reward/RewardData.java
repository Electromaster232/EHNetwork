package ehnetwork.core.reward;

import org.bukkit.inventory.ItemStack;

/**
 * Created by shaun on 14-09-18.
 */
public class RewardData
{
	private final String _friendlyName;
	private final ItemStack _displayItem;

	public RewardData(String friendlyName, ItemStack displayItem)
	{
		_friendlyName = friendlyName;
		_displayItem = displayItem;
	}

	public String getFriendlyName()
	{
		return _friendlyName;
	}

	public ItemStack getDisplayItem()
	{
		return _displayItem;
	}

}
