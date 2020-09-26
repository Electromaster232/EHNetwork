package ehnetwork.core.donation.command;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.donation.DonationManager;

public class GoldCommand extends CommandBase<DonationManager>
{
	public GoldCommand(DonationManager plugin)
	{
		super(plugin, Rank.ADMIN, "givegold");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			UtilPlayer.message(caller, F.main("Gold", "Your Gold: " + F.elem("" + Plugin.Get(caller).getGold())));
		}
		else if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("Gold", "Missing Args: " + F.elem("/gold <player> <amount>")));
			return;
		}

		final String targetName = args[0];
		final String goldString = args[1];
		Player target = UtilPlayer.searchExact(targetName);

		if (target == null)
		{
			Plugin.getClientManager().loadClientByName(targetName, new Runnable()
			{
				public void run()
				{
					CoreClient client = Plugin.getClientManager().Get(targetName);
					
					if (client != null)
						rewardGold(caller, null, targetName, client.getAccountId(), goldString);
					else
					{
						UtilPlayer.message(caller, F.main("Gold", "Could not find player " + F.name(targetName)));
					}
				}
			});
		}
		else
		{
			rewardGold(caller, target, target.getName(), Plugin.getClientManager().Get(target).getAccountId(), goldString);
		}
	}

	private void rewardGold(final Player caller, final Player target, final String targetName, final int accountId, String goldString)
	{
		try
		{
			int gold = Integer.parseInt(goldString);
			rewardGold(caller, target, targetName, accountId, gold);
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Gold", "Invalid Gold Amount"));
		}
	}

	private void rewardGold(final Player caller, final Player target, final String targetName, final int accountId, final int gold)
	{
		Plugin.RewardGold(new Callback<Boolean>()
		{
			public void run(Boolean completed)
			{
				UtilPlayer.message(caller, F.main("Gold", "You gave " + F.elem(gold + " Gold") + " to " + F.name(targetName) + "."));

				if (target != null)
				{
					UtilPlayer.message(target, F.main("Gold", F.name(caller.getName()) + " gave you " + F.elem(gold + " Gold") + "."));
				}
			}
		}, caller.getName(), targetName, accountId, gold);
	}
}
