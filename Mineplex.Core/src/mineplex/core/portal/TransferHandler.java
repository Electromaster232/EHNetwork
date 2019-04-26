package mineplex.core.portal;

import mineplex.serverdata.commands.CommandCallback;
import mineplex.serverdata.commands.ServerCommand;
import mineplex.serverdata.commands.ServerTransfer;
import mineplex.serverdata.commands.TransferCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
