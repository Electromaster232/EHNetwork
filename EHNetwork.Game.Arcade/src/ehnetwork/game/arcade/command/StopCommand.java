package ehnetwork.game.arcade.command;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;

public class StopCommand extends CommandBase<ArcadeManager>
{
	public StopCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.MAPLEAD, Rank.JNR_DEV}, "stop");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (Plugin.GetGame() == null)
			return; 

		if (Plugin.GetGame().GetState() == GameState.End || Plugin.GetGame().GetState() == GameState.End)
		{
			caller.sendMessage("Game is already ending..."); 
			return;
		}
		else if (Plugin.GetGame().GetState() == GameState.Recruit)
		{
			Plugin.GetGame().SetState(GameState.Dead);
		}
		else
		{
			Plugin.GetGame().SetState(GameState.End);
		}

		HandlerList.unregisterAll(Plugin.GetGame());

		Plugin.GetGame().Announce(C.cAqua + C.Bold + caller.getName() + " has stopped the game.");
	}
}
