package mineplex.hub.server;

import java.awt.Color;
import java.util.Comparator;

import org.bukkit.ChatColor;

public class ServerSorter implements Comparator<ServerInfo>
{
	private int _requiredSlots;
	
	public ServerSorter(int slots)
	{
		_requiredSlots = slots;
	}
	
	public int compare(ServerInfo a, ServerInfo b) 
	{
		if ((b.MOTD.contains("Restarting")))
			return -1;
		
		if ((a.MOTD.contains("Restarting")))
			return 1;
		
		if ((a.MOTD.contains("Recruiting") || a.MOTD.contains("Waiting") || a.MOTD.contains("Starting") || a.MOTD.contains("Cup")) && !b.MOTD.contains("Recruiting") && !b.MOTD.contains("Waiting") && !b.MOTD.contains("Starting") && !b.MOTD.contains("Cup"))
			return -1;

		if ((b.MOTD.contains("Recruiting") || b.MOTD.contains("Waiting") || b.MOTD.contains("Starting") || b.MOTD.contains("Cup")) && !a.MOTD.contains("Recruiting") && !a.MOTD.contains("Waiting") && !a.MOTD.contains("Starting") && !a.MOTD.contains("Cup"))
			return 1;
		
		if (a.MOTD.contains("Generating") && b.MOTD.contains("Generating"))
		{
			try
			{
				String aTime = ChatColor.stripColor(a.MOTD.substring(a.MOTD.indexOf("(") + 1, a.MOTD.indexOf(")")));
				String bTime = ChatColor.stripColor(b.MOTD.substring(b.MOTD.indexOf("(") + 1, b.MOTD.indexOf(")")));
				
				int timeOfA = (int)Double.parseDouble(aTime.split(" ")[0]) * (aTime.contains("Minute") ? 60 : 1);
				int timeOfB = (int)Double.parseDouble(bTime.split(" ")[0]) * (bTime.contains("Minute") ? 60 : 1);
				
				if (timeOfA < timeOfB)
					return -1;
				else if (timeOfB < timeOfA)
					return 1;
				else
					return 0;
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				return 0;
			}
		}
		
		if (a.MaxPlayers - a.CurrentPlayers < _requiredSlots && b.MaxPlayers - b.CurrentPlayers >= _requiredSlots)
			return -1;

		if (b.MaxPlayers - b.CurrentPlayers < _requiredSlots && a.MaxPlayers - a.CurrentPlayers >= _requiredSlots)
			return 1;
		
		if (a.CurrentPlayers > b.CurrentPlayers)
			return -1;
		
		if (b.CurrentPlayers > a.CurrentPlayers)
			return 1;
		
		if (Integer.parseInt(a.Name.split("-")[1]) < Integer.parseInt(b.Name.split("-")[1]))
			return -1;
		else if (Integer.parseInt(a.Name.split("-")[1]) > Integer.parseInt(b.Name.split("-")[1]))
			return 1;

		return 0;
	}
}