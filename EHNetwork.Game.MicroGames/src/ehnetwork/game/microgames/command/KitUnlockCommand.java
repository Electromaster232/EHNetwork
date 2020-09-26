package ehnetwork.game.microgames.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.game.microgames.MicroGamesManager;

public class KitUnlockCommand extends CommandBase<MicroGamesManager>
{
	public KitUnlockCommand(MicroGamesManager plugin)
	{
		super(plugin, Rank.OWNER, new Rank[] {Rank.YOUTUBE, Rank.TWITCH, Rank.FUCK}, new String[] {"youtube", "twitch", "kits"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.toggleUnlockKits(caller);
	}
}
