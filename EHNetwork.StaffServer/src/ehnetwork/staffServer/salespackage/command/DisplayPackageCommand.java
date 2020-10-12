package ehnetwork.staffServer.salespackage.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class DisplayPackageCommand extends CommandBase<SalesPackageManager>
{
	public DisplayPackageCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "display");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length < 2)
			return;
		
		String playerName = args[0];
		String packageName = args[1];
		
		if (args.length > 2)
		{
			for (int i = 2; i < args.length; i++)
			{
				packageName += " " + args[i];
			}
		}
		
		if (packageName.equalsIgnoreCase("ALL"))
			Plugin.displaySalesPackages(caller,  playerName);
		else
			Plugin.displayPackage(caller, playerName, packageName);
	}
}
