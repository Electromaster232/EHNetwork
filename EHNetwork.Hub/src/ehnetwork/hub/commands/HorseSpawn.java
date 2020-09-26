package ehnetwork.hub.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.hub.modules.AdminMountManager;

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
