package ehnetwork.staffServer.helpdesk;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.staffServer.helpdesk.repository.ApiGetCall;

public class HelpDeskManager extends MiniPlugin
{
	public HelpDeskManager(JavaPlugin plugin)
	{
		super("Help Desk", plugin);
		
		new ApiGetCall("https://mineplex.jitbit.com/helpdesk/api", "Tickets&");
	}
}
