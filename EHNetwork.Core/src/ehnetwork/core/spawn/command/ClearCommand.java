package ehnetwork.core.spawn.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.spawn.Spawn;

public class ClearCommand extends CommandBase<Spawn>
{
	public ClearCommand(Spawn plugin)
	{
		super(plugin, Rank.ADMIN, "clear");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.ClearSpawn(caller);
	}
}
