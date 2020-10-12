package ehnetwork.hub.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.hub.modules.ForcefieldManager;

public class ForcefieldRadius extends CommandBase<ForcefieldManager>
{
	public ForcefieldRadius(ForcefieldManager plugin)
	{
		super(plugin, Rank.OWNER, new String[] {"radius"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.ForcefieldRadius(caller, args);
	}
}
