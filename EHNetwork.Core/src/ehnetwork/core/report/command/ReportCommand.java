package ehnetwork.core.report.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.report.ReportManager;
import ehnetwork.core.report.ReportPlugin;

public class ReportCommand extends CommandBase<ReportPlugin>
{
	
	public ReportCommand(ReportPlugin plugin)
	{
		super(plugin, Rank.ALL, "report");
	}
	
	@Override
	public void Execute(final Player player, final String[] args)
	{		
		if(args == null || args.length < 2)
		{
			UtilPlayer.message(player, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
		else
		{
			String playerName = args[0];
			Player reportedPlayer = UtilPlayer.searchOnline(player, playerName, false);
			String reason = F.combine(args, 1, null, false);
			
			if (reportedPlayer != null)
			{
				ReportManager.getInstance().reportPlayer(player, reportedPlayer, reason);	
			}
			else
			{
				UtilPlayer.message(player, F.main(Plugin.getName(), C.cRed + "Unable to find player '"
									+ playerName + "'!"));
			}
		}
	}
}
