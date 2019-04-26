package mineplex.hub.notification.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.hub.mail.MailManager;
import mineplex.hub.mail.MailMessage;
import mineplex.hub.mail.PlayerMailData;
import mineplex.hub.notification.NotificationManager;

public class NotificationCommand extends CommandBase<NotificationManager>
{
	public NotificationCommand(NotificationManager plugin)
	{
		super(plugin, Rank.ALL, "notifications");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.openShop(caller);
	}
}
