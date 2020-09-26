package ehnetwork.hub.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.hub.HubManager;

public class NewsConfirmCommand extends CommandBase<HubManager>
{
	public NewsConfirmCommand(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, "¢¤₦₣¡₨₥");
	}
	
	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0 || args.length > 1)
		{
			UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
		else
		{
			int newsPosition;
			try
			{
				newsPosition = Integer.parseInt(args[0]);
			}
			catch (Exception exception)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "The specified news position is invalid!"));
				return;
			}

			UtilServer.getServer().dispatchCommand(UtilServer.getServer().getConsoleSender(), "tellraw " + caller.getName() + " {\"text\":\"" + Plugin.getName() + "> \", color:blue, \"extra\":[{\"text\":\"[CONFIRM] \", color:green, \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/news delete " + newsPosition + "\"}, \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Are you absolutely sure???\"}}, {\"text\":\"News Entry " + newsPosition + "\", color:gold}, {\"text\":\" deletion?\", color:gray}]}");
			return;
		}
	}
}
