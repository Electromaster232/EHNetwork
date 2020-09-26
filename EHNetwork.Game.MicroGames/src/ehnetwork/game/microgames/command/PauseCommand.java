package ehnetwork.game.microgames.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.MicroGamesManager;

public class PauseCommand extends CommandBase<MicroGamesManager>
{
	private MicroGamesManager _manager;
	public PauseCommand(MicroGamesManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {}, "pause");
		
		_manager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean doPause = _manager.GetRotationPause();
		if(!doPause){
			_manager.SetRotationPause(true);
			UtilPlayer.message(caller, F.main("Manager", "The rotation has been paused."));
		}
		else {
			_manager.SetRotationPause(false);
			UtilPlayer.message(caller, F.main("Manager", "The rotation has been unpaused."));
		}
	}
}
