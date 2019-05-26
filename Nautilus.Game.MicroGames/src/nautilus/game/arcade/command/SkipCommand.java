package nautilus.game.arcade.command;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game;

public class SkipCommand extends CommandBase<ArcadeManager>
{
	private ArcadeManager _manager;
	public SkipCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {}, "skip");
		
		_manager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		_manager.GetGame().SetState(Game.GameState.End);
	}
}
