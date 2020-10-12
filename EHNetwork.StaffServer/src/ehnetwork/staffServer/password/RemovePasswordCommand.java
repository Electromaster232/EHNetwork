package ehnetwork.staffServer.password;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;

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
