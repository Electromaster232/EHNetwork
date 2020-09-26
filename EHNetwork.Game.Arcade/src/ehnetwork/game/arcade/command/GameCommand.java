package ehnetwork.game.arcade.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.MultiCommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.ArcadeManager;

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
