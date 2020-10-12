package ehnetwork.core.stats.command;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.stats.PlayerStats;
import ehnetwork.core.stats.StatsManager;

/**
 * Created by Shaun on 10/2/2014.
 */
public class TimeCommand extends CommandBase<StatsManager>
{
	public TimeCommand(StatsManager plugin)
	{
		super(plugin, Rank.MODERATOR, "time");
	}

	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args == null || args.length == 0)
		{
			UtilPlayer.message(caller, F.main("Time", "Usage: /time <playerName>"));
		}
		else
		{
			Player target = UtilPlayer.searchOnline(caller, args[0], false);

			if (target == null)
			{
				Plugin.getPlugin().getServer().getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							final PlayerStats stats = Plugin.getOfflinePlayerStats(args[0]);

							Plugin.getPlugin().getServer().getScheduler().runTask(Plugin.getPlugin(), new Runnable()
							{
								@Override
								public void run()
								{
									if (stats == null)
									{
										UtilPlayer.message(caller, F.main("Time", "Player " + F.elem(args[0]) + " not found!"));
									}
									else
									{
										long time = stats.getStat("Global.TimeInGame");
										UtilPlayer.message(caller, F.main("Time", F.name(args[0]) + " has spent " + F.elem(UtilTime.convertString(time * 1000L, 1, UtilTime.TimeUnit.FIT)) + " in game"));
									}
								}
							});
						}
						catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
				});
			}
			else
			{
				long time = Plugin.Get(target).getStat("Global.TimeInGame");
				UtilPlayer.message(caller, F.main("Time", F.name(target.getName()) + " has spent " + F.elem(UtilTime.convertString(time * 1000L, 1, UtilTime.TimeUnit.FIT)) + " in game"));
			}
		}
	}
}