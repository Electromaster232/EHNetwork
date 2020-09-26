package ehnetwork.core.reward.rewards;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.Callback;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.reward.Reward;
import ehnetwork.core.reward.RewardData;
import ehnetwork.core.reward.RewardRarity;

public class CoinReward extends Reward
{
	private DonationManager _donationManager;
	private Random _random;
	private int _minCoinCount;
	private int _maxCoinCount;

	public CoinReward(DonationManager donationManager, int minCoinCount, int maxCoinCount, int weight, RewardRarity rarity)
	{
		this(donationManager, minCoinCount, maxCoinCount, weight, rarity, RANDOM);
	}

	public CoinReward(DonationManager donationManager, int minCoinCount, int maxCoinCount, int weight, RewardRarity rarity, Random random)
	{
		super(rarity, weight);
		_donationManager = donationManager;
		_minCoinCount = minCoinCount;
		_maxCoinCount = maxCoinCount;

		_random = random;
	}

	@Override
	public RewardData giveRewardCustom(Player player)
	{
		int gemsToReward = _random.nextInt(_maxCoinCount - _minCoinCount) + _minCoinCount;

		_donationManager.RewardCoins(new Callback<Boolean>()
		{
			@Override
			public void run(Boolean data)
			{

			}
		}, "Treasure Chest", player.getName(), _donationManager.getClientManager().Get(player).getAccountId(), gemsToReward);

		return new RewardData(getRarity().getColor() + gemsToReward + " Coins", new ItemStack(175));
	}

	@Override
	public boolean canGiveReward(Player player)
	{
		return true;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CoinReward)
			return true;

		return false;
	}
}
