package ehnetwork.staffServer.password;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;

public class CreatePasswordCommand extends CommandBase<Password>
{
	public CreatePasswordCommand(Password plugin)
	{
		super(plugin, Rank.ADMIN, "createpassword");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null && args.length == 1)
		{
			resetCommandCharge(caller);
			Plugin.createPassword(caller, args[0]);
		}
	}
}
