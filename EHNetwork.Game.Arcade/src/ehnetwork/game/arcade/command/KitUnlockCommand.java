package ehnetwork.game.arcade.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.game.arcade.ArcadeManager;

public class KitUnlockCommand extends CommandBase<ArcadeManager>
{
	public KitUnlockCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.OWNER, new Rank[] {Rank.YOUTUBE, Rank.TWITCH, Rank.FUCK}, new String[] {"youtube", "twitch", "kits"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.toggleUnlockKits(caller);
	}
}
