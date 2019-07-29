package nautilus.game.survival.commands;


import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilAction;
import nautilus.game.survival.SurvivalManager;


public class DoubleJumpCommand extends CommandBase<SurvivalManager> implements Listener
{

	public DoubleJumpCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.LEGEND, new Rank[]{}, new String[]{"launch","l"});
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		caller.setFlying(false);

		//Disable Flight
		caller.setAllowFlight(false);

		//Velocity
		UtilAction.velocity(caller, 4, 2, 20, true);

		//Sound
		caller.playEffect(caller.getLocation(), Effect.BLAZE_SHOOT, 0);
	}
}