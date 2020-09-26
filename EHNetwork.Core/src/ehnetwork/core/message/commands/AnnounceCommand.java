package ehnetwork.core.message.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.message.MessageManager;
import ehnetwork.serverdata.commands.AnnouncementCommand;

public class AnnounceCommand extends CommandBase<MessageManager>
{
	public AnnounceCommand(MessageManager plugin)
	{
		super(plugin, Rank.ADMIN, "announce");
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
			new AnnouncementCommand(true, F.combine(args, 0, null, false)).publish();
		}
	}
}
