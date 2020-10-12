package ehnetwork.core.message.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.message.MessageManager;
import ehnetwork.serverdata.commands.AnnouncementCommand;

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
