package ehnetwork.staffServer.salespackage.command;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class CoinCommand extends CommandBase<SalesPackageManager>
{
	public CoinCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "coin");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null || args.length != 2)
			return;
		
		final String playerName = args[0];
		final int amount = Integer.parseInt(args[1]);
		
		Plugin.getClientManager().loadClientByName(playerName, new Runnable()
		{
			public void run()
			{
				CoreClient client = Plugin.getClientManager().Get(playerName);
				
				if (client != null)
				{
					Plugin.getDonationManager().RewardCoins(new Callback<Boolean>()
					{
						public void run(Boolean completed)
						{
							if (completed)
							{
								caller.sendMessage(F.main(Plugin.getName(), "Added " + amount + " coins to " + playerName + "'s account!"));
							}
							else
							{
								UtilPlayer.message(caller, F.main(Plugin.getName(), "There was an error giving " + F.elem(amount + "Coins") + " to " + F.name(playerName) + "."));
							}
						}
					}, caller.getName(), playerName, client.getAccountId(), amount);
					
				}
				else
					caller.sendMessage(F.main(Plugin.getName(), "Couldn't find " + playerName + "'s account!"));
			}
		});
	}
}
