package nautilus.game.arcade.command;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import mineplex.core.command.MultiCommandBase;
import mineplex.core.common.Rank;

public class GameCommand extends MultiCommandBase<ArcadeManager>
{
	public GameCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.MAPLEAD, Rank.JNR_DEV}, "game");
		
		AddCommand(new StartCommand(Plugin));
		AddCommand(new StopCommand(Plugin));
		AddCommand(new SetCommand(Plugin));
	}

	@Override
	protected void Help(Player caller, String[] args)
	{
		UtilPlayer.message(caller, F.main(Plugin.getName(), "Commands List:"));
		UtilPlayer.message(caller, F.help("/game start", "Start the current game", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/game stop", "Stop the current game", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/game set <GameType> (MapSource) (Map)", "Set the current game or next game", Rank.ADMIN));
		UtilPlayer.message(caller, F.main("Tip", "Use TAB for games/maps!"));
	}
}
