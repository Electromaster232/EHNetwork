package ehnetwork.core.reward.rewards;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.Rank;
import ehnetwork.core.reward.Reward;
import ehnetwork.core.reward.RewardData;
import ehnetwork.core.reward.RewardRarity;

public class RankReward extends Reward
{
	private CoreClientManager _clientManager;

	public RankReward(CoreClientManager clientManager, int weight, RewardRarity rarity)
	{
		super(rarity, weight);
		
		_clientManager = clientManager;
	}

	@Override
	public RewardData giveRewardCustom(Player player)
	{
		Rank rank = null;
		if (_clientManager.Get(player).GetRank() == Rank.ALL)			rank = Rank.ULTRA;
		else if (_clientManager.Get(player).GetRank() == Rank.ULTRA)	rank = Rank.HERO;
		else if (_clientManager.Get(player).GetRank() == Rank.HERO)		rank = Rank.LEGEND;
		
		if (rank == null)
			return new RewardData(getRarity().getColor() + "Rank Upgrade Error", new ItemStack(Material.PAPER));
		
		_clientManager.Get(player).SetRank(rank);
		_clientManager.getRepository().saveRank(null, player.getName(), player.getUniqueId(), rank, true);
		
		return new RewardData(getRarity().getColor() + rank.Name + " Rank", new ItemStack(Material.NETHER_STAR));
	}

	@Override
	public boolean canGiveReward(Player player)
	{
		return !_clientManager.Get(player).GetRank().Has(Rank.LEGEND);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof RankReward)
			return true;

		return false;
	}
}
