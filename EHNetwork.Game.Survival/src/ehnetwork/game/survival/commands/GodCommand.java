package ehnetwork.game.survival.commands;


import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.game.survival.SurvivalManager;


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

