package ehnetwork.core.reward.rewards;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.reward.Reward;
import ehnetwork.core.reward.RewardData;
import ehnetwork.core.reward.RewardRarity;

/**
 * Created by shaun on 14-09-12.
 */
public class InventoryReward extends Reward
{
	private Random _random;

	private InventoryManager _inventoryManager;
	private ItemStack _itemStack;
	private String _name;
	private String _packageName;
	private int _minAmount;
	private int _maxAmount;

	public InventoryReward(InventoryManager inventoryManager, String name, String packageName, int minAmount, int maxAmount, ItemStack itemStack, RewardRarity rarity, int weight)
	{
		this(RANDOM, inventoryManager, name, packageName, minAmount, maxAmount, itemStack, rarity, weight);
	}

	public InventoryReward(Random random, InventoryManager inventoryManager, String name, String packageName, int minAmount, int maxAmount, ItemStack itemStack, RewardRarity rarity, int weight)
	{
		super(rarity, weight);

		_random = random;
		_name = name;
		_packageName = packageName;
		_minAmount = minAmount;
		_maxAmount = maxAmount;
		_itemStack = itemStack;
		_inventoryManager = inventoryManager;
	}

	@Override
	public RewardData giveRewardCustom(Player player)
	{
		int amountToGive;

		if (_minAmount != _maxAmount)
		{
			amountToGive = _random.nextInt(_maxAmount - _minAmount) + _minAmount;
		}
		else
		{
			amountToGive = _minAmount;
		}

		_inventoryManager.addItemToInventory(player, "Item", _packageName, amountToGive);

		return new RewardData(getRarity().getColor() + amountToGive + " " + _name, _itemStack);
	}

	@Override
	public boolean canGiveReward(Player player)
	{
		return true;
	}

	protected String getPackageName()
	{
		return _packageName;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof InventoryReward)
		{
			return ((InventoryReward) obj).getPackageName().equals(_packageName);
		}
		return false;
	}
}
