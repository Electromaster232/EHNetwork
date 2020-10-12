package ehnetwork.core.updater;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.portal.Portal;
import ehnetwork.serverdata.commands.RestartCommand;

public class RestartServerCommand extends CommandBase<FileUpdater>
{
	public RestartServerCommand(FileUpdater plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, "restart");
	}

	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args == null || args.length < 1)
		{
			UtilPlayer.message(caller, F.main("Restart", "You must specify a server to restart."));
			return;
		}
		
		Portal.getInstance().doesServerExist(args[0], new Callback<Boolean>()
		{
			public void run(Boolean serverExists)
			{
				if (serverExists)
				{
					new RestartCommand(args[0], Plugin.getRegion()).publish();
					UtilPlayer.message(caller, F.main("Restart", "Sent restart command to " + C.cGold + args[0] + C.cGray + "."));
				}
				else
					UtilPlayer.message(caller, F.main("Restart", C.cGold + args[0] + C.cGray + " doesn't exist."));
			}
		});
	}
}
