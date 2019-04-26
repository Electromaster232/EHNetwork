package mineplex.core.chat.command;

import org.bukkit.entity.Player;

import mineplex.core.chat.Chat;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilServer;

public class BroadcastCommand extends CommandBase<Chat>
{
	public BroadcastCommand(Chat plugin)
	{
		super(plugin, Rank.MODERATOR, "s");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		String announcement = "";

		for (String arg : args)
			announcement += arg + " ";

		if (announcement.length() > 0)
			announcement = announcement.substring(0, announcement.length()-1);

		UtilServer.broadcast(caller.getName(), announcement);
	}
}
