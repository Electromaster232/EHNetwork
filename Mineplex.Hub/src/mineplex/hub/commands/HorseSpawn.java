package mineplex.hub.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.hub.modules.AdminMountManager;

public class HorseSpawn extends CommandBase<AdminMountManager>
{
	public HorseSpawn(AdminMountManager plugin)
	{
		super(plugin, Rank.OWNER, new String[] {"horse"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.HorseCommand(caller, args);
	}
}
