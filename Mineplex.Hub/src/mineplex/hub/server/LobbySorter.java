package mineplex.hub.server;

import java.util.Comparator;

public class LobbySorter implements Comparator<ServerInfo>
{	
	public int compare(ServerInfo a, ServerInfo b)
	{
		if (Integer.parseInt(a.Name.split("-")[1]) < Integer.parseInt(b.Name.split("-")[1]))
			return -1;

		return 1;
	}
}