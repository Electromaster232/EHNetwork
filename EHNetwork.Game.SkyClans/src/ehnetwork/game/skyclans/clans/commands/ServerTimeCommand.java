package ehnetwork.game.skyclans.clans.commands;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.skyclans.clans.ClansManager;

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
