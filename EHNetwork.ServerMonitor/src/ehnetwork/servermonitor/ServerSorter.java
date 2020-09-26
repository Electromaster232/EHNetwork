package ehnetwork.servermonitor;

import java.util.Comparator;

import ehnetwork.serverdata.data.MinecraftServer;

public class ServerSorter implements Comparator<MinecraftServer>
{
	@Override
	public int compare(MinecraftServer first, MinecraftServer second)
	{
		if (Integer.parseInt(first.getName().split("-")[1]) < Integer.parseInt(second.getName().split("-")[1]))
			return -1;
		else if (Integer.parseInt(second.getName().split("-")[1]) < Integer.parseInt(first.getName().split("-")[1]))
			return 1;

		return 0;
	}
}
