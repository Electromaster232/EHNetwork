package ehnetwork.core.spawn.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.spawn.Spawn;

public class AddCommand extends CommandBase<Spawn>
{
	public AddCommand(Spawn plugin)
	{
		super(plugin, Rank.ADMIN, "add", "a");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.AddSpawn(caller);
	}
}
