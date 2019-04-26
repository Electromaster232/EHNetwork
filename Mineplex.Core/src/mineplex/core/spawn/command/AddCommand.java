package mineplex.core.spawn.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.spawn.Spawn;

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
