package ehnetwork.core.donation.command;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.donation.DonationManager;

public class CoinCommand extends CommandBase<DonationManager>
{
	public CoinCommand(DonationManager plugin)
	{
		super(plugin, Rank.ADMIN, "coin");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Coin", "Missing Args: " + F.elem("/coin <player> <amount>")));
			return;
		}

		final String targetName = args[0];
		final String coinsString = args[1];
		Player target = UtilPlayer.searchExact(targetName);

		if (target == null)
		{
			Plugin.getClientManager().loadClientByName(targetName, new Runnable()
			{
				public void run()
				{
					CoreClient client = Plugin.getClientManager().Get(targetName);
					
					if (client != null)
						rewardCoins(caller, null, targetName, client.getAccountId(), coinsString);
					else
					{
						UtilPlayer.message(caller, F.main("Coin", "Could not find player " + F.name(targetName)));
					}
				}
			});
		}
		else
		{
			rewardCoins(caller, target, target.getName(), Plugin.getClientManager().Get(target).getAccountId(), coinsString);
		}
	}

	private void rewardCoins(final Player caller, final Player target, final String targetName, final int accountId, String coinsString)
	{
		try
		{
			int coins = Integer.parseInt(coinsString);
			rewardCoins(caller, target, targetName, accountId, coins);
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Coin", "Invalid Coins Amount"));
		}
	}

	private void rewardCoins(final Player caller, final Player target, final String targetName, final int accountId, final int coins)
	{
		Plugin.RewardCoins(new Callback<Boolean>()
		{
			public void run(Boolean completed)
			{
				if (completed)
				{
					UtilPlayer.message(caller, F.main("Coin", "You gave " + F.elem(coins + " Coins") + " to " + F.name(targetName) + "."));
	
					if (target != null)
					{
						UtilPlayer.message(target, F.main("Coin", F.name(caller.getName()) + " gave you " + F.elem(coins + " Coins") + "."));
					}
				}
				else
				{
					UtilPlayer.message(caller, F.main("Coin", "There was an error giving " + F.elem(coins + "Coins") + " to " + F.name(targetName) + "."));
				}
			}
		}, caller.getName(), targetName, accountId, coins);
	}
}
