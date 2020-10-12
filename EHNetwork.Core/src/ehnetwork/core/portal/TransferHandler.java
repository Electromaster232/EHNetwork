package ehnetwork.core.portal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;
import ehnetwork.serverdata.commands.ServerTransfer;
import ehnetwork.serverdata.commands.TransferCommand;

public class TransferHandler implements CommandCallback 
{
	public void run(ServerCommand command)
	{
		if (command instanceof TransferCommand)
		{
			TransferCommand transferCommand = (TransferCommand) command;
			ServerTransfer transfer = transferCommand.getTransfer();
			
			Player player = Bukkit.getPlayerExact(transfer.getPlayerName());
			
			if (player != null && player.isOnline())
			{
				Portal.getInstance().sendPlayerToServer(player, transfer.getServerName());
			}
		}
	}
}
