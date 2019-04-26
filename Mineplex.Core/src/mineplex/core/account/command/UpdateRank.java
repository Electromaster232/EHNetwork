package mineplex.core.account.command;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.common.util.UtilPlayer;

public class UpdateRank extends CommandBase<CoreClientManager>
{
	public UpdateRank(CoreClientManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV /*On test servers only*/}, "updateRank");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		boolean testServer = Plugin.getPlugin().getConfig().getString("serverstatus.group").equalsIgnoreCase("Testing");

		if (Plugin.Get(caller).GetRank() == Rank.JNR_DEV && !testServer)
		{
			F.main(Plugin.getName(), F.elem(Rank.JNR_DEV.GetTag(true, true)) + "s are only permitted to set ranks on test servers!");
			return;
		}

		if (args == null)
		{			
			UtilPlayer.message(caller, F.main(Plugin.getName(), "/" + AliasUsed + " joeschmo MODERATOR"));
		}
		else
		{
			if (args.length == 0)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), "Player argument missing."));
				return;
			}

			final String playerName = args[0];
			Rank tempRank = null;
			
			try
			{
				tempRank = Rank.valueOf(args[1]);
			}
			catch (Exception ex)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), ChatColor.RED + "" + ChatColor.BOLD + "Invalid rank!"));
				return;
			}
			
			final Rank rank = tempRank;
			 
			if (rank == Rank.ADMIN || rank == Rank.YOUTUBE || rank == Rank.TWITCH || rank == Rank.MODERATOR || rank == Rank.HELPER || rank == Rank.ALL || rank == Rank.MAPDEV || rank == Rank.SNR_MODERATOR || rank == Rank.JNR_DEV || rank == Rank.DEVELOPER)
			{
				if (!testServer && rank.Has(Rank.ADMIN) && !Plugin.hasRank(caller, Rank.LT))
				{
					UtilPlayer.message(caller, F.main(Plugin.getName(), ChatColor.RED + "" + ChatColor.BOLD + "Insufficient privileges!"));
					return;
				}
				
				Plugin.getRepository().matchPlayerName(new Callback<List<String>>()
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
								
								UUID uuid = Plugin.loadUUIDFromDB(playerName);

								if (uuid == null)
									uuid = UUIDFetcher.getUUIDOf(playerName);
								
								Plugin.getRepository().saveRank(new Callback<Rank>()
								{
									public void run(Rank rank)
									{
										caller.sendMessage(F.main(Plugin.getName(), target + "'s rank has been updated to " + rank.Name + "!"));
									}
								}, target, uuid, rank, true);
								
							}
						}, caller, playerName, true);
					}
				}, playerName);
			}
		}
	}
}
