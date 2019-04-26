package nautilus.game.arcade.command;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;

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
