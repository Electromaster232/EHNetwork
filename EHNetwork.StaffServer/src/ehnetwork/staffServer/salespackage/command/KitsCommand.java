package ehnetwork.staffServer.salespackage.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class KitsCommand extends CommandBase<SalesPackageManager>
{
	public KitsCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "kits");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length != 1)
			return;
		
		String playerName = args[0];
		
		Plugin.getDonationManager().applyKits(playerName);
		caller.sendMessage(F.main(Plugin.getName(), "Unlocked kits for " + playerName + "'s account!"));
	}
}
