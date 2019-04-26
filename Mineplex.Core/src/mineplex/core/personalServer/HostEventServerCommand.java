package mineplex.core.personalServer;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.recharge.Recharge;

public class HostEventServerCommand extends CommandBase<PersonalServerManager>
{
	public HostEventServerCommand(PersonalServerManager plugin)
	{
		super(plugin, Rank.ADMIN, "hostevent");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (!Recharge.Instance.use(caller, "Host Event", 30000, false, false))
			return;
		
		Plugin.hostServer(caller, caller.getName(), true);
	}
}
