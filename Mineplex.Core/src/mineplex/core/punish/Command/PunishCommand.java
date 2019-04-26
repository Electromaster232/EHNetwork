package mineplex.core.punish.Command;

import java.util.Iterator;
import java.util.List;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.punish.Punish;
import mineplex.core.punish.Tokens.PunishClientToken;
import mineplex.core.punish.UI.PunishPage;

public class PunishCommand extends CommandBase<Punish>
{
	public PunishCommand(Punish plugin)
	{
		super(plugin, Rank.HELPER, "punish", "p");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{		
		if (args == null || args.length < 2)
		{
			Plugin.Help(caller);
		}
		else
		{
			final String playerName = args[0];
			String reason = args[1];
			
			for (int i = 2; i < args.length; i++)
			{
				reason += " " + args[i];
			}
			
			final String finalReason = reason;
			
			//Match exact online first
			Player target = UtilPlayer.searchExact(playerName);
			if (target != null)
			{
				Plugin.GetRepository().LoadPunishClient(playerName, new Callback<PunishClientToken>()
				{
					public void run(PunishClientToken clientToken)
					{
						Plugin.LoadClient(clientToken);
						new PunishPage(Plugin, caller, playerName, finalReason);
					}
				});
				
				return;
			}
			
			//Check repo
			Plugin.GetRepository().MatchPlayerName(new Callback<List<String>>()
			{
				public void run(List<String> matches)
				{
					boolean matchedExact = false;
					
					for (String match : matches)
					{
						if (match.equalsIgnoreCase(playerName))
						{
							matchedExact = true;
						}
					}
					
					if (matchedExact)
					{
						for (Iterator<String> matchIterator = matches.iterator(); matchIterator.hasNext();)
						{
							if (!matchIterator.next().equalsIgnoreCase(playerName))
							{
								matchIterator.remove();
							}
						}
					}
					
					UtilPlayer.searchOffline(matches, new Callback<String>()
					{
						public void run(final String target)
						{
							if (target == null)
							{
								return;
							}
							
							Plugin.GetRepository().LoadPunishClient(target, new Callback<PunishClientToken>()
							{
								public void run(PunishClientToken clientToken)
								{
									Plugin.LoadClient(clientToken);
									new PunishPage(Plugin, caller, target, finalReason);
								}
							});
							
						}
					}, caller, playerName, true);
				}
			}, playerName);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args)
	{
		return args.length == 1 ? getPlayerMatches((Player)sender, args[0]) : null;
	}
}
