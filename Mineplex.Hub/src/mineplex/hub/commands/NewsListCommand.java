package mineplex.hub.commands;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.hub.HubManager;
import mineplex.hub.modules.NewsManager;

public class NewsListCommand extends CommandBase<HubManager>
{
	public NewsListCommand(HubManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, "list");
	}
	
	@Override
	public void Execute(final Player caller, String[] args)
	{		
		if (args == null || args.length == 0)
		{
			final NewsManager newsMang = Plugin.GetNewsManager();
			
			UtilPlayer.message(caller, F.main(Plugin.getName(), C.cGray + "Current server news messages:"));
			
			newsMang.RetriveNewsEntries(new Callback<HashMap<String, String>>()
			{
				public void run(final HashMap<String, String> newsEntries)
				{
					// Order newsEntries set or its output by newsPosition, not hash order...
					newsMang.RetrieveMaxNewsPosition(new Callback<Integer>()
					{
						public void run(Integer maxPosition)
						{
							String[] newsStrings = new String[maxPosition];
							for (Iterator<String> iterator = newsEntries.keySet().iterator(); iterator.hasNext();)
							{
								String newsPosition = iterator.next();							
								newsStrings[Integer.parseInt(newsPosition) - 1] = newsEntries.get(newsPosition);
							}	
						
							for (int i = 0; i < newsStrings.length; i++)
							{
								UtilServer.getServer().dispatchCommand(UtilServer.getServer().getConsoleSender(), "tellraw " + caller.getName() + " {\"text\":\"" + Plugin.getName() + "> \", color:blue, \"extra\":[{\"text\":\"[DELETE] \", color:red, \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/news ¢¤₦₣¡₨₥ " + (i + 1) + "\"}, \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Deletes News Entry " + (i + 1) + " : " + newsStrings[i] + "\"}}, {\"text\":\"News " + (i + 1) + "\", color:gold}, {\"text\":\" : \", color:gray}, {\"text\":\"" + newsStrings[i] + "\", color:white}]}");
							}
						}
					});
				}
			});
			return;
		}
		else
		{
			UtilPlayer.message(caller, F.main(Plugin.getName(), C.cRed + "Your arguments are inappropriate for this command!"));
			return;
		}
	}
}
