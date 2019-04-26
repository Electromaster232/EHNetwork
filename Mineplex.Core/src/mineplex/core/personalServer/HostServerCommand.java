package mineplex.core.personalServer;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.recharge.Recharge;

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
