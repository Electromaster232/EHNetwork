package ehnetwork.game.microgames.command;


import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.MicroGamesManager;


public class FlyCommand extends CommandBase<MicroGamesManager>
{
	public FlyCommand(MicroGamesManager plugin)
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
