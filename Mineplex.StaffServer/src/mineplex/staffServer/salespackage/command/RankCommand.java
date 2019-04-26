package mineplex.staffServer.salespackage.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.staffServer.salespackage.SalesPackageManager;

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
		
		final Rank rankEnum = Rank.valueOf(rank);
		 
		if (rankEnum == Rank.HERO || rankEnum == Rank.ULTRA || rankEnum == Rank.LEGEND || rankEnum == Rank.ALL)
		{
			Plugin.getClientManager().SaveRank(playerName, uuid, mineplex.core.common.Rank.valueOf(rank), perm);
			caller.sendMessage(F.main(Plugin.getName(), playerName + "'s rank has been updated to " + rank + "!"));
		}
	}
}
