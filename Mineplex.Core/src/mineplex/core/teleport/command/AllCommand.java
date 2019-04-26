package mineplex.core.teleport.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.teleport.Teleport;

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
