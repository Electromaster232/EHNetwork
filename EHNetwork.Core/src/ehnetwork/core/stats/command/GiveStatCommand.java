package ehnetwork.core.stats.command;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClient;
import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.stats.StatsManager;

public class GiveStatCommand extends CommandBase<StatsManager>
{
	public GiveStatCommand(StatsManager plugin)
	{
		super(plugin, Rank.OWNER, "givestat");
	}

	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Stats", "/givestat <Target> <Name> <Amount>"));
			return;
		}
		
		try
		{
			Player player = UtilPlayer.searchOnline(caller, args[0], true);

			String tempStatName = args[1];
			
			for (int i = 2; i < args.length - 1; i++)
			{
				tempStatName += " " + args[i];
			}
			
			final String statName = tempStatName;
			
			if (player == null)
			{
				Plugin.getClientManager().loadClientByName(args[0], new Runnable()
				{
					public void run()
					{
						final CoreClient client = Plugin.getClientManager().Get(args[0]);
						
						if (client != null)
							Plugin.incrementStat(client.getAccountId(), statName, Long.parseLong(args[args.length - 1]));
						else
							caller.sendMessage(F.main(Plugin.getName(), "Couldn't find " + args[0] + "'s account!"));
					}
				});
			}
			else
			{
				Plugin.incrementStat(player, statName, Integer.parseInt(args[args.length - 1]));
			}
			
			UtilPlayer.message(caller, F.main("Stats", "Applied " + F.elem(Integer.parseInt(args[args.length - 1]) + " " + statName) + " to " + F.elem(player.getName()) + "."));
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Stats", "/givestat <Name> <Amount>"));
		}
	}
}