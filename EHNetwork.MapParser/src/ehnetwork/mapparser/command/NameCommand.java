package ehnetwork.mapparser.command;

import org.bukkit.World;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.mapparser.MapData;
import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class NameCommand extends BaseCommand
{

	public NameCommand(MapParser plugin)
	{
		super(plugin, "name");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		World world = player.getWorld();

		if (args.length < 1)
		{
			message(player, "Invalid Input. " + F.elem("/name <MapName>"));
			return true;
		}

		if (world.equals("world"))
		{
			message(player, "Cannot set name for Lobby.");
			return true;
		}

		String mapName = "";
		for(String arg : args)
			mapName += arg + " ";

		mapName = mapName.trim();

		//Permission
		if (!getPlugin().GetData(world.getName()).HasAccess(player))
		{
			message(player, "You do not have Build-Access on this Map.");
			return true;
		}

		MapData data = getPlugin().GetData(world.getName());

		data.MapName = mapName;
		data.Write();

		getPlugin().Announce("Map Name for " + F.elem(world.getName()) + " set to " + F.elem(mapName) + ".");

		return true;
	}
}
