package ehnetwork.staffServer.password;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;

public class PasswordCommand extends CommandBase<Password>
{
	public PasswordCommand(Password plugin)
	{
		super(plugin, Rank.MODERATOR, "pass", "password");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args != null && args.length == 1)
		{
			resetCommandCharge(caller);
			Plugin.checkPassword(caller, args[0]);
		}
	}
}
