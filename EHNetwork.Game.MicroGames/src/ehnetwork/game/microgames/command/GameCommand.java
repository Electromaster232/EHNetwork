package ehnetwork.game.microgames.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.MultiCommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.MicroGamesManager;

public class GameCommand extends MultiCommandBase<MicroGamesManager>
{
	public GameCommand(MicroGamesManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {}, "game");
		

		AddCommand(new SetCommand(Plugin));
		AddCommand(new PauseCommand(Plugin));
		AddCommand(new SkipCommand(Plugin));
	}

	@Override
	protected void Help(Player caller, String[] args)
	{
		UtilPlayer.message(caller, F.main(Plugin.getName(), "Commands List:"));
		UtilPlayer.message(caller, F.help("/game pause", "Pause/Unpause the Micro Games Rotation", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/game skip", "Skip the current game", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/game set <GameType> (MapSource) (Map)", "Set the next game", Rank.ADMIN));
		UtilPlayer.message(caller, F.main("Tip", "Use TAB for games/maps!"));
	}
}
