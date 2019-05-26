package nautilus.game.arcade.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.arcade.ArcadeManager;

public class PauseCommand extends CommandBase<ArcadeManager>
{
	private ArcadeManager _manager;
	public PauseCommand(ArcadeManager plugin)
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
