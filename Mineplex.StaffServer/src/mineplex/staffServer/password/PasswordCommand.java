package mineplex.staffServer.password;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;

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
