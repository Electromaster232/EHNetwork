package ehnetwork.game.microgames.command;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game;

public class SkipCommand extends CommandBase<MicroGamesManager>
{
	private MicroGamesManager _manager;
	public SkipCommand(MicroGamesManager plugin)
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
