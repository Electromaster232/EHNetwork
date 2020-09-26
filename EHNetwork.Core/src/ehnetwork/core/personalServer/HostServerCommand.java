package ehnetwork.core.personalServer;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.recharge.Recharge;

public class HostServerCommand extends CommandBase<PersonalServerManager>
{
	public HostServerCommand(PersonalServerManager plugin)
	{
		super(plugin, Rank.LEGEND, "hostserver");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (!Recharge.Instance.use(caller, "Host Server", 30000, false, false))
			return;
		
		Plugin.hostServer(caller, caller.getName(), false);
	}
}
