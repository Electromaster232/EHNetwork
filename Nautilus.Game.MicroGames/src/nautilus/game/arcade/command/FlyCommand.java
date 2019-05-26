package nautilus.game.arcade.command;


import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.arcade.ArcadeManager;


public class FlyCommand extends CommandBase<ArcadeManager>
{
	public FlyCommand(ArcadeManager plugin)
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
