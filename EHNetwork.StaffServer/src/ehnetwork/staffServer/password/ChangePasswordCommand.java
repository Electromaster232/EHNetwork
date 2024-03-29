package ehnetwork.staffServer.password;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;

public class ChangePasswordCommand extends CommandBase<Password>
{
	public ChangePasswordCommand(Password plugin)
	{
		super(plugin, Rank.ADMIN, "changepassword");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null && args.length == 1)
		{
			Plugin.changePassword(caller, args[0]);
		}
	}
}
