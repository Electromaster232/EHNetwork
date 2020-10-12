package ehnetwork.core.chat.command;

import org.bukkit.entity.Player;

import ehnetwork.core.chat.Chat;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;

public class SilenceCommand extends CommandBase<Chat>
{
	public SilenceCommand(Chat plugin)
	{
		super(plugin, Rank.ADMIN, "silence");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		try
		{
			//Toggle
			if (args.length == 0)
			{
				//Disable
				if (Plugin.Silenced() != 0)
				{
					Plugin.Silence(0, true);
				}
				//Enable
				else
				{
					Plugin.Silence(-1, true);
				}
			}
			//Timer
			else
			{
				long time = (long) (Double.valueOf(args[0]) * 3600000);
				
				Plugin.Silence(time, true);
			}
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Chat", "Invalid Time Parameter."));
		}
	}
}
