package ehnetwork.core.report.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.report.ReportManager;
import ehnetwork.core.report.ReportPlugin;

public class ReportHandleCommand extends CommandBase<ReportPlugin>
{
	
	public ReportHandleCommand(ReportPlugin plugin)
	{
		super(plugin, Rank.ADMIN, "reporthandle", "rh");
	}
	
	@Override
	public void Execute(final Player player, final String[] args)
	{		
		if(args == null || args.length < 1)
		{
			UtilPlayer.message(player, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
		else
		{
			int reportId = Integer.parseInt(args[0]);
			
			ReportManager.getInstance().handleReport(reportId, player);
		}
	}
}
