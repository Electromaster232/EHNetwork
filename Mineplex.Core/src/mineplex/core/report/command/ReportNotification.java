package mineplex.core.report.command;

import org.bukkit.entity.Player;

import mineplex.core.common.util.UtilServer;
import mineplex.core.report.ReportManager;
import mineplex.serverdata.commands.ServerCommand;

public class ReportNotification extends ServerCommand
{
	
	// TODO: Encode in JSON-interactive chat message
	private String notification;
	
	public ReportNotification(String notification)
	{
		super();	// Send to all servers
	}
	
	public void run()
	{
		// Message all players that can receive report notifications.
		for (Player player : UtilServer.getPlayers())
		{
			if (ReportManager.getInstance().hasReportNotifications(player))
			{
				player.sendMessage(notification);
			}
		}
	}
}
