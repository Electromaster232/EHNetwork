package nautilus.game.survival.commands;


import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.survival.SurvivalManager;


public class GodCommand extends CommandBase<SurvivalManager>
{
	public GodCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.MODERATOR, new Rank[]{Rank.YOUTUBE, Rank.TWITCH}, new String[]{"heal"});
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		caller.setHealth(20);
		caller.setFoodLevel(20);
		caller.setSaturation(20);
	}

}

