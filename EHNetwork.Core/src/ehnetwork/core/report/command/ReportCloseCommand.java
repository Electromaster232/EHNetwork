package ehnetwork.core.report.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.report.ReportManager;
import ehnetwork.core.report.ReportPlugin;

public class ReportCloseCommand extends CommandBase<ReportPlugin>
{
	
	public ReportCloseCommand(ReportPlugin plugin)
	{
		super(plugin, Rank.ADMIN, "reportclose", "rc");
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
			int reportId = Integer.parseInt(args[0]);
			String reason = F.combine(args, 1, null, false);
			
			ReportManager.getInstance().closeReport(reportId, player, reason);
		}
	}
}
