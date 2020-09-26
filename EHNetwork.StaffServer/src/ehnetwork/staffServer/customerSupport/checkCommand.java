package ehnetwork.staffServer.customerSupport;

import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.Callback;

public class checkCommand extends CommandBase<CustomerSupport>
{
	public checkCommand(CustomerSupport plugin)
	{
		super(plugin, Rank.MODERATOR, "check", "c");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null || args.length != 1)
		{
			Plugin.Help(caller);
		}
		else
		{
			String playerName = args[0];

			CommandCenter.GetClientManager().checkPlayerName(caller, playerName, new Callback<String>()
			{
				public void run(final String name)
				{
					if (name != null)
					{
						CommandCenter.GetClientManager().loadClientByName(name, new Runnable()
						{
							public void run()
							{
								Plugin.addAgentMapping(caller, name);
								Plugin.showPlayerInfo(caller, name);
							}
						});
					}
				}
			});
		}
	}
}
