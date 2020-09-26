package ehnetwork.core.teleport.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.teleport.Teleport;

public class LocateCommand extends CommandBase<Teleport>
{
	public LocateCommand(Teleport plugin)
	{
		super(plugin, Rank.MODERATOR, "locate", "where", "find");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			UtilPlayer.message(caller, F.main("Locate", "Player argument missing."));
			return;
		}
		
		Plugin.locatePlayer(caller, args[0]);
	}
}
