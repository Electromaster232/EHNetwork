package ehnetwork.core.loot;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RandomItem
{
	private int _amount;
	private ItemStack _item;
	private int _min, _max;

	public RandomItem(ItemStack item, int amount)
	{
		this(item, amount, item.getAmount(), item.getAmount());
	}

	public RandomItem(ItemStack item, int amount, int minStackSize, int maxStackSize)
	{
		_amount = amount;
		_item = item;
		_min = minStackSize;
		_max = maxStackSize;
	}

	public RandomItem(Material material, int amount)
	{
		this(material, amount, 1, 1);
	}

	public RandomItem(Material material, int amount, int minStackSize, int maxStackSize)
	{
		_amount = amount;
		_item = new ItemStack(material);
		_min = minStackSize;
		_max = maxStackSize;
	}

	public int getAmount()
	{
		return _amount;
	}

	public ItemStack getItemStack()
	{
		_item.setAmount((new Random().nextInt(Math.max(1, (_max - _min) + 1)) + _min));

		return _item;
	}
}