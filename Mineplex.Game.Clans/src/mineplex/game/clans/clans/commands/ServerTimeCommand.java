package mineplex.game.clans.clans.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilPlayer;
import mineplex.game.clans.clans.ClansManager;

public class ServerTimeCommand extends CommandBase<ClansManager>
{
	public ServerTimeCommand(ClansManager plugin)
	{
		super(plugin, Rank.ALL, "servertime", "t");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		UtilPlayer.message(caller, Plugin.getWarManager().mServerTime());
	}
}
