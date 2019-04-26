package mineplex.staffServer.customerSupport;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;

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
