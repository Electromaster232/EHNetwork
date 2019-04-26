package mineplex.core.portal.Commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.portal.Portal;

public class ServerCommand extends CommandBase<Portal>
{
	public ServerCommand(Portal plugin)
	{
		super(plugin, Rank.ALL, "server");
	}

	@Override
	public void Execute(final Player player, final String[] args)
	{
		final Rank playerRank = CommandCenter.GetClientManager().Get(player).GetRank();
		final String serverName = Plugin.getPlugin().getConfig().getString("serverstatus.name");

		if (args == null || args.length == 0)
		{
			UtilPlayer.message(player,
					F.main(Plugin.getName(), C.cGray + "You are currently on server: " + C.cGold + serverName));
			return;
		}
		else if (args.length == 1)
		{
			if (serverName.equalsIgnoreCase(args[0]))
			{
				UtilPlayer.message(
						player,
						F.main(Plugin.getName(), "You are already on " + C.cGold + serverName + C.cGray
								+ "!"));
			}
			else
			{
				Plugin.doesServerExist(args[0], new Callback<Boolean>()
				{
					public void run(final Boolean serverExists)
					{						
						if (!serverExists)
						{
							UtilPlayer.message(
									player,
									F.main(Plugin.getName(), "Server " + C.cGold + args[0]
											+ C.cGray + " does not exist!"));
							return;
						}

						boolean deniedAccess = false;
						String servUp = args[0].toUpperCase();

						if (servUp.contains("HERO"))
						{
							if (playerRank.Has(Rank.HERO))
								Plugin.sendPlayerToServer(player, args[0]);
							else
								deniedAccess = true;
						}
						else if (servUp.contains("ULTRA") || servUp.contains("BETA"))
						{
							if (playerRank.Has(Rank.ULTRA))
								Plugin.sendPlayerToServer(player, args[0]);
							else
								deniedAccess = true;
						}
						else if (servUp.contains("STAFF"))
						{
							if (playerRank.Has(Rank.HELPER))
								Plugin.sendPlayerToServer(player, args[0]);
							else
								deniedAccess = true;
						}
						else if (servUp.contains("TEST"))
						{
							if (playerRank.Has(Rank.MODERATOR))
								Plugin.sendPlayerToServer(player, args[0]);
							else
								deniedAccess = true;
						}
						else
						{
							Plugin.sendPlayerToServer(player, args[0]);
						}
						
						if (deniedAccess)
						{
							UtilPlayer.message(
									player,
									F.main(Plugin.getName(), C.cRed + "You don't have permission to join " + C.cGold + args[0]));
						}
					}
				});
			}
		}
		else
		{
			UtilPlayer.message(player,
					F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
	}
}
