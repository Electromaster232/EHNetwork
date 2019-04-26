package mineplex.core.spawn.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.spawn.Spawn;

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
