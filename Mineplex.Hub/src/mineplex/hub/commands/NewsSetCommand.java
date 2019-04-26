package mineplex.hub.commands;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.hub.HubManager;
import mineplex.hub.modules.NewsManager;

public class NewsSetCommand extends CommandBase<HubManager>
{
	public NewsSetCommand(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, "set");
	}
	
	@Override
	public void Execute(final Player caller, final String[] args)
	{
		if (args == null || args.length < 2 || args.length > 128)
		{
			UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
		else
		{
			NewsManager newsMang = Plugin.GetNewsManager();
			
			final int newsPosition;
			String newsEntry = "";
			for (int i = 1; i < args.length; i++)
			{
				newsEntry += args[i] + " ";
			}
			newsEntry = newsEntry.substring(0, newsEntry.length() - 1);

			// Check for 256 character length for MySQL!
			if (newsEntry.length() > 256)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "The specified news entry is too long [> 256 characters]!"));
				return;
			}
			
			try
			{
				newsPosition = Integer.parseInt(args[0]);
			}
			catch (Exception exception)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "The specified news position is invalid!"));
				return;
			}
			newsMang.SetNewsEntry(newsEntry, newsPosition, new Callback<Boolean>()
			{
				public void run(Boolean success)
				{
					if (success)
					{
						String newsEntry = "";
						for (int i = 1; i < args.length; i++)
						{
							newsEntry += args[i] + " ";
						}
						newsEntry = newsEntry.substring(0, newsEntry.length() - 1);
						
						UtilPlayer.message(caller, F.main(Plugin.getName(), C.cGray + "The news entry at position " + C.cGold + newsPosition + C.cGray + " has been updated to: " + C.cGold + newsEntry + C.cGray + "!"));
					}
					else
					{
						UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "There was an error updating the news entry; likely the specified news position was invalid!"));
					}
				}
			});
			return;
		}
	}
}
