package ehnetwork.hub.notification.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.hub.notification.NotificationManager;

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
