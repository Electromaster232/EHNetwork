package mineplex.core.message.redis;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.serverdata.commands.AnnouncementCommand;
import mineplex.serverdata.commands.CommandCallback;
import mineplex.serverdata.commands.ServerCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
