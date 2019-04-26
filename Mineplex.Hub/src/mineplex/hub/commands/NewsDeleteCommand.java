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

public class NewsDeleteCommand extends CommandBase<HubManager>
{
	public NewsDeleteCommand(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, "delete");
	}
	
	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args == null || args.length == 0 || args.length > 1)
		{
			UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
		else
		{
			NewsManager newsMang = Plugin.GetNewsManager();
			final int newsPosition;
			try
			{
				newsPosition = Integer.parseInt(args[0]);
			}
			catch (Exception exception)
			{
				UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "The specified news position is invalid!"));
				return;
			}
			newsMang.DeleteNewsEntry(newsPosition, new Callback<Boolean>()
			{
				public void run(Boolean success)
				{
					if (success)
					{							
						UtilPlayer.message(caller, F.main(Plugin.getName(), C.cGray + "The news entry at position " + C.cGold + newsPosition + C.cGray + " has been deleted!"));
					}
					else
					{
						UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "There was an error deleting the news entry; likely the specified news position was invalid!"));
					}
				}
			});
			return;
		}
	}
}
