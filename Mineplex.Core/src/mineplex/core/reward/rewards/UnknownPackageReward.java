package mineplex.core.reward.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.donation.DonationManager;
import mineplex.core.reward.Reward;
import mineplex.core.reward.RewardData;
import mineplex.core.reward.RewardManager;
import mineplex.core.reward.RewardRarity;

/**
 * Created by shaun on 14-09-12.
 */
public class UnknownPackageReward extends Reward
{
	private DonationManager _donationManager;
	private ItemStack _itemStack;
	private String _name;
	private String _packageName;

	public UnknownPackageReward(DonationManager donationManager, String name, String packageName, ItemStack itemStack, RewardRarity rarity, int weight)
	{
		super(rarity, weight);
		_donationManager = donationManager;
		_name = name;
		_packageName = packageName;
		_itemStack = itemStack;
	}

	@Override
	protected RewardData giveRewardCustom(Player player)
	{
		_donationManager.PurchaseUnknownSalesPackage(null, player.getName(), _donationManager.getClientManager().Get(player).getAccountId(), _packageName, true, 0, true);

		return new RewardData(getRarity().getColor() + _name, _itemStack);
	}

	@Override
	public boolean canGiveReward(Player player)
	{
		if (_donationManager.Get(player.getName()) == null)
		{
			System.out.println("Could not give reward " + _packageName + " to Offline Player: " + player.getName());
			return false;
		}
		
		return !_donationManager.Get(player.getName()).OwnsUnknownPackage(_packageName);
	}

	protected String getPackageName()
	{
		return _packageName;
	}

	protected String getFriendlyName()
	{
		return _name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof UnknownPackageReward)
		{
			return ((UnknownPackageReward) obj).getPackageName().equals(_packageName);
		}
		return false;
	}
}
