package mineplex.core.reward;

import java.util.Random;

import org.bukkit.entity.Player;

/**
 * Created by Shaun on 9/2/2014.
 */
public abstract class Reward
{
	protected static final Random RANDOM = new Random();

	private RewardRarity _rarity;
	private int _weight;

	public Reward(RewardRarity rarity, int weight)
	{
		_rarity = rarity;
		_weight = weight;
	}

	public final RewardData giveReward(String type, Player player)
	{
		return giveRewardCustom(player);
	}

	protected abstract RewardData giveRewardCustom(Player player);

	public abstract boolean canGiveReward(Player player);

	public RewardRarity getRarity()
	{
		return _rarity;
	}

	public int getWeight()
	{
		return _weight;
	}
}
