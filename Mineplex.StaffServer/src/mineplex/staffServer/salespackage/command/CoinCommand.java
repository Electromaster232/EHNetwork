package mineplex.staffServer.salespackage.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import mineplex.core.account.CoreClient;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.common.util.UtilPlayer;
import mineplex.staffServer.salespackage.SalesPackageManager;

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
