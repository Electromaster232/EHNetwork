package ehnetwork.staffServer.salespackage.command;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class GemHunterCommand extends CommandBase<SalesPackageManager>
{
	public GemHunterCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "gemhunter");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null || args.length != 2)
			return;
		
		final String playerName = args[0];
		final int amount = Integer.parseInt(args[1]);
		int tempExp = 0;

		if (amount == 4)
			tempExp = 70000;
		else if (amount == 8)
			tempExp = 220000;
		
		final long experience = tempExp;
		
		Plugin.getClientManager().loadClientByName(playerName, new Runnable()
		{
			public void run()
			{
				CoreClient client = Plugin.getClientManager().Get(playerName);
				
				if (client != null)
				{
					Plugin.getDonationManager().PurchaseUnknownSalesPackage(null, playerName, client.getAccountId(), "Gem Hunter Level " + amount, false, 0, false);
					Plugin.getStatsManager().incrementStat(client.getAccountId(), "Global.GemsEarned", experience);
					caller.sendMessage(F.main(Plugin.getName(), "Added Level " + amount + " Gem Hunter to " + playerName + "'s account!"));
				}
				else
					caller.sendMessage(F.main(Plugin.getName(), "Couldn't find " + playerName + "'s account!"));
			}
		});			
	}
}
