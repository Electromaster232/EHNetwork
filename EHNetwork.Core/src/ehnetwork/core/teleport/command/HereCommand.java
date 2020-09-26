package ehnetwork.core.teleport.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.teleport.Teleport;

public class HereCommand extends CommandBase<Teleport>
{
	public HereCommand(Teleport plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, "here", "h");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args.length == 1)
			Plugin.playerToPlayer(caller, args[0], caller.getName());
		else if (args.length == 2)
			Plugin.playerToPlayer(caller, args[0], args[1]);
	}
}
