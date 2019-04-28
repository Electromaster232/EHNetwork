package nautilus.game.arcade.command;

import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;

public class KitUnlockCommand extends CommandBase<ArcadeManager>
{
	public KitUnlockCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.OWNER, new Rank[] {Rank.YOUTUBE, Rank.TWITCH, Rank.ANNOYING_KID}, new String[] {"youtube", "twitch", "kits", "annoyingkid"});
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.toggleUnlockKits(caller);
	}
}
