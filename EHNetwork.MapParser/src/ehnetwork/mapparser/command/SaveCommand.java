package ehnetwork.mapparser.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/16/2014.
 */
public class SaveCommand extends BaseCommand
{
	public SaveCommand(MapParser plugin)
	{
		super(plugin, "save");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length < 1)
		{
			message(player, "Invalid Input. " + F.elem("/save <MapName>"));
			return true;
		}

		String name = args[0];

		List<String> possibleMaps = getPlugin().getMapsByName(name);

		if (possibleMaps.size() > 1)
		{
			message(player, "More than one map found:");
			for (String s : possibleMaps)
				UtilPlayer.message(player, s);

			return true;
		}
		else if (possibleMaps.size() == 0)
		{
			message(player, "No maps found with the name: " + F.elem(name));
			return true;
		}

		String worldName = possibleMaps.get(0);
		World world = getPlugin().GetMapWorld(worldName);

		if (world != null)
		{
			if (!getPlugin().GetData(worldName).HasAccess(player))
			{
				message(player, "You do not have Build-Access on this Map.");
				return true;
			}

			//Teleport Out
			for (Player other : world.getPlayers())
				other.teleport(getPlugin().getSpawnLocation());

			//Unload World
			Bukkit.getServer().unloadWorld( world, true);
		}
		else
		{
			message(player, "World is not loaded: " + F.elem(worldName));
			return true;
		}

		getPlugin().Announce("Saved World: " + F.elem(args[0]));

		return true;
	}
}
