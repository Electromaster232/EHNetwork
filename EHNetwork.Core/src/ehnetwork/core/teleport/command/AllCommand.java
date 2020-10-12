package ehnetwork.core.teleport.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.teleport.Teleport;

public class AllCommand extends CommandBase<Teleport>
{
	public AllCommand(Teleport plugin)
	{
		super(plugin, Rank.OWNER, "all");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.playerToPlayer(caller, "%ALL%", caller.getName());
	}
}
