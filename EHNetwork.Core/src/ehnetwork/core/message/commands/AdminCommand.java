package ehnetwork.core.message.commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.message.MessageManager;

public class AdminCommand extends CommandBase<MessageManager>
{
	public AdminCommand(MessageManager plugin)
	{
		super(plugin, Rank.ALL, "a","admin");
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
			if (args.length == 0)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), "Message argument missing."));
				return;
			}
			
			if (Plugin.isMuted(caller))
			{
				return;
			}

			//Parse Message
			String message = F.combine(args, 0, null, false);

			//Inform
			UtilPlayer.message(caller, F.rank(Plugin.GetClientManager().Get(caller).GetRank()) + " " + caller.getName() + " " + C.cPurple + message);

			//Send
			boolean staff = false;
			for (Player to : UtilServer.getPlayers())
			{
				if (Plugin.GetClientManager().Get(to).GetRank().Has(Rank.HELPER))
				{
					if (!to.equals(caller))
						UtilPlayer.message(to, F.rank(Plugin.GetClientManager().Get(caller).GetRank()) + " " + caller.getName() + " " + C.cPurple + message);

					staff = true;

					//Sound
					to.playSound(to.getLocation(), Sound.NOTE_PLING, 0.5f, 2f);
				}
			}
		
			if (!staff)
				UtilPlayer.message(caller, F.main(Plugin.getName(), "There are no Staff Members online."));

			//Log XXX
			//Logger().logChat("Staff Chat", from, staff, message);
		}
	}
}
