package mineplex.staffServer.password;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;

public class RemovePasswordCommand extends CommandBase<Password>
{
	public RemovePasswordCommand(Password plugin)
	{
		super(plugin, Rank.ADMIN, "removepassword");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		resetCommandCharge(caller);
		Plugin.removePassword(caller);
	}
}
