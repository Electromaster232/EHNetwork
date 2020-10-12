package ehnetwork.game.survival.commands;


import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.survival.SurvivalManager;


public class FlyCommand extends CommandBase<SurvivalManager>
{
	public FlyCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.MODERATOR, new Rank[]{Rank.YOUTUBE, Rank.TWITCH}, new String[]{"fly"});
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean hideMe = caller.getAllowFlight();
		if(hideMe){
			UtilPlayer.message(caller, F.main("Flight", "Fly disabled."));
		}
		else{
			UtilPlayer.message(caller, F.main("Flight", "Fly enabled."));
		}

			if (hideMe)
			{
				caller.setAllowFlight(false);
			}
			else
			{
				caller.setAllowFlight(true);
			}
		}
	}
