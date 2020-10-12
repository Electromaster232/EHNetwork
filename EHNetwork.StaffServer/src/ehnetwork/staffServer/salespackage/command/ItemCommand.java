package ehnetwork.staffServer.salespackage.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.server.util.TransactionResponse;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class ItemCommand extends CommandBase<SalesPackageManager>
{
	public ItemCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "item");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null)
			return;
		
		final String playerName = args[0];
		int amountSpecified = Integer.parseInt(args[1]);
		final String category = args[2];
		String tempName = args[3];
		
		for (int i = 4; i < args.length; i++)
		{
			tempName += " " + args[i];
		}
		
		final String itemName = tempName;
		final int amount = amountSpecified;
		
		if (!Plugin.getInventoryManager().validCategory(category))
		{
			caller.sendMessage(F.main(Plugin.getName(), "You have entered an invalid Category."));
			return;
		}
		
		if (!Plugin.getInventoryManager().validItem(itemName))
		{
			caller.sendMessage(F.main(Plugin.getName(), "You have entered an invalid Item."));
			return;
		}
		
		Plugin.getClientManager().loadClientByName(playerName, new Runnable()
		{
			public void run()
			{
				final UUID uuid = Plugin.getClientManager().loadUUIDFromDB(playerName);
				final CoreClient client = Plugin.getClientManager().Get(playerName);
				
				if (uuid != null)
				{
					Plugin.getDonationManager().PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
					{
						public void run(TransactionResponse data)
						{
							Plugin.getInventoryManager().addItemToInventoryForOffline(new Callback<Boolean>()
							{
								public void run(Boolean success)
								{
									if (success)
										UtilPlayer.message(caller, F.main(Plugin.getName(), playerName + " received " + amount + " " + itemName + "."));
									else
									{
										UtilPlayer.message(caller, F.main(Plugin.getName(), "ERROR processing " + playerName + " " + amount + " " + itemName + "."));
									}
								}
							}, uuid, category, itemName, amount);
						}
					}, playerName, client.getAccountId(), (amount == 1 ? itemName : itemName + " " + amount), false, 0, false);
				}
				else
					caller.sendMessage(F.main(Plugin.getName(), "Couldn't find " + playerName + "'s account!"));
			}
		});
	}
}
