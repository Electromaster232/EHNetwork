package mineplex.core.report.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.portal.Portal;
import mineplex.core.report.ReportManager;
import mineplex.core.report.ReportPlugin;

import org.bukkit.entity.Player;

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
