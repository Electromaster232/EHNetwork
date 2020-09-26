package ehnetwork.core.punish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.punish.Tokens.PunishClientToken;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.PunishCommand;
import ehnetwork.serverdata.commands.ServerCommand;

public class PunishmentHandler implements CommandCallback
{
	private Punish _punishManager;
	
	public PunishmentHandler(Punish punishManager)
	{
		_punishManager = punishManager;
	}
	
	public void run(ServerCommand command)
	{
		if (command instanceof PunishCommand)
		{
			PunishCommand punishCommand = (PunishCommand)command;
			
			String playerName = punishCommand.getPlayerName();
			boolean ban = punishCommand.getBan();
			final String reason = punishCommand.getMessage();
			final Player player = Bukkit.getPlayer(playerName);
			
			if (player != null && player.isOnline())
			{
				if (ban)
				{
					Bukkit.getServer().getScheduler().runTask(_punishManager.getPlugin(), new Runnable()
					{
						public void run()
						{
							player.kickPlayer(reason);
						}
					});
				}
				else
				{
					_punishManager.GetRepository().LoadPunishClient(playerName, new Callback<PunishClientToken>()
					{
						public void run(PunishClientToken token)
						{
							_punishManager.LoadClient(token);
							UtilPlayer.message(player, reason);
						}
					});
				}
			}
		}
	}
}
