package mineplex.core.spawn.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.command.MultiCommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.spawn.Spawn;

public class SpawnCommand extends MultiCommandBase<Spawn>
{
	public SpawnCommand(Spawn plugin)
	{
		super(plugin, Rank.ADMIN, "spawn");
		
		AddCommand(new AddCommand(plugin));
		AddCommand(new ClearCommand(plugin));
	}

	@Override
	public void Help(Player caller, String[] args)
	{
		UtilPlayer.message(caller, F.main("Spawn", "Commands List:"));
		UtilPlayer.message(caller, F.help("/spawn add", "Add Location as Spawn", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/spawn clear", "Remove All Spawns", Rank.ADMIN));
	}
}
