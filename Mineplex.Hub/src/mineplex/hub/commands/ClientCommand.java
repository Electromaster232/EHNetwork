package mineplex.hub.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.hub.HubManager;

public class ClientCommand extends CommandBase<HubManager>
{
	public ClientCommand(HubManager plugin)
	{
		super(plugin, Rank.ALL, new Rank[] {}, new String[] {"ihaveleclient"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		UtilPlayer.message(caller, F.main("EHNetwork", "Thank you for using the EHNetwork Client! You will receive monthly rewards for doing so!"));
		UtilServer.broadcast(C.cGreen + C.Bold + caller.getName() + " has joined using the custom EHNetwork Client! Download it today for monthly rewards!");
	}
}
