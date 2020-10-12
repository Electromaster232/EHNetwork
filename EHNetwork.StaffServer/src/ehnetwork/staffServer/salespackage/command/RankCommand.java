package ehnetwork.staffServer.salespackage.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UUIDFetcher;
import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class RankCommand extends CommandBase<SalesPackageManager>
{
	public RankCommand(SalesPackageManager plugin)
	{
		super(plugin, Rank.MODERATOR, "rank");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length != 3)
			return;
		
		String playerName = args[0];
		String rank = args[1];
		boolean perm = Boolean.parseBoolean(args[2]);
		
		UUID uuid = Plugin.getClientManager().loadUUIDFromDB(playerName);

		if (uuid == null)
			uuid = UUIDFetcher.getUUIDOf(playerName);

		try {
			Plugin.getClientManager().SaveRank(playerName, uuid, Rank.valueOf(rank), perm);
			caller.sendMessage(F.main(Plugin.getName(), playerName + "'s rank has been updated to " + rank + "!"));
		} catch (IllegalArgumentException e) {
			caller.sendMessage(F.main(Plugin.getName(), "The rank you have entered does not exist"));
		}

	}
}
