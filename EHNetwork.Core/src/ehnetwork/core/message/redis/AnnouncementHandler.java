package ehnetwork.core.message.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.serverdata.commands.AnnouncementCommand;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;

public class AnnouncementHandler implements CommandCallback
{
	public void run(ServerCommand command)
	{
		if (command instanceof AnnouncementCommand)
		{
			AnnouncementCommand announcementCommand = (AnnouncementCommand)command;
			
			String message = announcementCommand.getMessage();
			
			if (announcementCommand.getDisplayTitle())
				UtilTextMiddle.display(C.cYellow + "Announcement", message, 10, 120, 10);
			
			for (Player player : Bukkit.getOnlinePlayers())
			{
				UtilPlayer.message(player, F.main("Announcement", C.cAqua + message));
			}
		}
	}
}
