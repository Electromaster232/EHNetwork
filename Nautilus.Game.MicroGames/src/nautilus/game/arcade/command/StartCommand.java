package nautilus.game.arcade.command;

import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;

public class StartCommand extends CommandBase<ArcadeManager>
{
	public StartCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.MAPLEAD, Rank.JNR_DEV}, "start");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (Plugin.GetGame() == null)
			return;
		
		if (Plugin.GetGame().GetState() != GameState.Recruit)
		{
			caller.sendMessage("Game is already in progress...");
			return;
		}

		int seconds;
		if(args != null && args.length > 0)
			seconds = Integer.parseInt(args[0]);
		else 
			seconds = 10;

		Plugin.GetGameManager().StateCountdown(Plugin.GetGame(), seconds, true);

		Plugin.GetGame().Announce(C.cAqua + C.Bold + caller.getName() + " has started the game.");
	}
}
