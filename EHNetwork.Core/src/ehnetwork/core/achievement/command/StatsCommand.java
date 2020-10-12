package ehnetwork.core.achievement.command;

import org.bukkit.entity.Player;

import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.UtilPlayer;

public class StatsCommand extends CommandBase<AchievementManager>
{
	public StatsCommand(AchievementManager plugin)
	{
		super(plugin, Rank.ALL, "stats");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args == null || args.length == 0)
		{
			Plugin.openShop(caller);
		}
		else
		{
			Player target = UtilPlayer.searchOnline(caller, args[0], true);

			if (target == null)
			{
				return;
			}

			Plugin.openShop(caller, target);
		}
	}
}
