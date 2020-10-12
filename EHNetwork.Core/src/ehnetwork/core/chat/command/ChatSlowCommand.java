package ehnetwork.core.chat.command;

import org.bukkit.entity.Player;

import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;

public class ChatSlowCommand extends CommandBase<Chat>
{
	public ChatSlowCommand(Chat plugin)
	{
		super(plugin, Rank.SNR_MODERATOR, "chatslow");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null && args.length == 1)
		{
			try
			{
				int seconds = Integer.parseInt(args[0]);

				if (seconds < 0)
				{
					UtilPlayer.message(caller, F.main("Chat", "Seconds must be a positive integer"));
					return;
				}

				Plugin.setChatSlow(seconds, true);
				UtilPlayer.message(caller, F.main("Chat", "Set chat slow to " + F.time(seconds + " seconds")));
			}
			catch (Exception e)
			{
				showUsage(caller);
			}
		}
		else
		{
			showUsage(caller);
		}
	}

	private void showUsage(Player caller)
	{
		UtilPlayer.message(caller, F.main("Chat", "Usage: /chatslow <seconds>"));
	}
}
