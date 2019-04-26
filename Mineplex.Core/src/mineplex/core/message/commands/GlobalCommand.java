package mineplex.core.message.commands;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.message.MessageManager;
import mineplex.serverdata.commands.AnnouncementCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GlobalCommand extends CommandBase<MessageManager>
{
	public GlobalCommand(MessageManager plugin)
	{
		super(plugin, Rank.ADMIN, "global");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null)
		{			
			Plugin.Help(caller);
		}
		else
		{
			new AnnouncementCommand(false, F.combine(args, 0, null, false)).publish();
		}
	}
}
