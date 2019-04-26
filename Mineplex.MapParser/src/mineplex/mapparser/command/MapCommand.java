package mineplex.mapparser.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.mapparser.GameType;
import mineplex.mapparser.MapData;
import mineplex.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class MapCommand extends BaseCommand
{
	public MapCommand(MapParser plugin)
	{
		super(plugin, "map");

		setDescription("Teleport to a map");
		setUsage("/map <name> [gametype]");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length < 1)
		{
			//UtilPlayer.message(event.getPlayer(), F.main("Parser", "Invalid Input. " + F.elem("/map <MapName> [GameType]")));
			return false;
		}

		String worldName = null;
		// Look up maps without a specific game type
		if (args.length == 1)
		{
			List<String> possibleMaps = getPlugin().getMapsByName(args[0]);
			if (possibleMaps.size() == 0)
			{
				message(player, "No maps found with the name: " + F.elem(args[0]));
			}
			else if (possibleMaps.size() > 1)
			{
				message(player, "Found more than one possible match:");
				for (String s : possibleMaps)
					UtilPlayer.message(player, s);

				return true;
			}

			worldName = possibleMaps.get(0);
		}
		else // Get map with specified name and gametype
		{
			GameType gameType = null;
			try
			{
				gameType = GameType.valueOf(args[1]);
			}
			catch (Exception e)
			{
				getPlugin().sendValidGameTypes(player);
				return true;
			}

			worldName = getPlugin().getWorldString(args[0], gameType);
		}

		if (getPlugin().getMapsBeingZipped().contains(worldName))
		{
			message(player, "That map is being backed up now. Try again soon");
			return true;
		}

		World world = getPlugin().GetMapWorld(worldName);
		if (world == null)
		{
			if (getPlugin().DoesMapExist(worldName))
			{
				world = Bukkit.createWorld(new WorldCreator(worldName));
			}
			else
			{
				message(player, "Map Not Found: " + F.elem(worldName));
				return true;
			}
		}

		//Error (This should not occur!)
		if (world == null)
		{
			message(player, "Null World Error: " + F.elem(worldName));
			return true;
		}

		//Permission
		if (!getPlugin().GetData(world.getName()).CanJoin(player))
		{
			message(player, "You do not have Join-Access on this Map.");
			return true;
		}

		//Teleport
		message(player, "Teleporting to World: " + F.elem(worldName));

		player.teleport(new Location(world, 0, 106, 0));

		MapData data = getPlugin().GetData(worldName);

		UtilPlayer.message(player, F.value("Map Name", data.MapName));
		UtilPlayer.message(player, F.value("Author", data.MapCreator));
		UtilPlayer.message(player, F.value("Game Type", data.MapGameType.GetName()));
		return true;
	}
}
