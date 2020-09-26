package ehnetwork.core.treasure;

import org.bukkit.Material;

import ehnetwork.core.common.util.C;
import ehnetwork.core.reward.RewardType;

public enum TreasureType
{
	OLD(C.cYellow + "Old Chest", "Old Chest", RewardType.OldChest, Material.CHEST, TreasureStyle.OLD),
	
	ANCIENT(C.cGold + "Ancient Chest", "Ancient Chest", RewardType.AncientChest, Material.TRAPPED_CHEST, TreasureStyle.ANCIENT),
	
	MYTHICAL(C.cRed + "Mythical Chest", "Mythical Chest", RewardType.MythicalChest, Material.ENDER_CHEST, TreasureStyle.MYTHICAL);

	private final String _name;
	private final RewardType _rewardType;
	private final Material _material;
	private final TreasureStyle _treasureStyle;
	private final String _itemName;

	TreasureType(String name, String itemName, RewardType rewardType, Material material, TreasureStyle treasureStyle)
	{
		_name = name;
		_itemName = itemName;
		_rewardType = rewardType;
		_material = material;
		_treasureStyle = treasureStyle;
	}

	public RewardType getRewardType()
	{
		return _rewardType;
	}

	public String getName()
	{
		return _name;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public TreasureStyle getStyle()
	{
		return _treasureStyle;
	}

	public String getItemName()
	{
		return _itemName;
	}
}
