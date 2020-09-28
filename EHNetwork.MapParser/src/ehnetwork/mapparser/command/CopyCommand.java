package ehnetwork.mapparser.command;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import net.minecraft.util.org.apache.commons.io.FileUtils;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.mapparser.GameType;
import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/16/2014.
 */
public class CopyCommand extends BaseCommand
{
	public CopyCommand(MapParser plugin)
	{
		super(plugin, "copy");
		setUsage("/copy <map name> <game type> <copy map name> <copy game time>");
		setDescription("Copy the data of a map into a new map. This preserves Build List.");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length != 4)
			return false;

		String originalMapName = args[0];
		String newMapName = args[2];

		GameType originalGametype = null;
		GameType newGameType = null;

		try
		{
			originalGametype = GameType.valueOf(args[1]);
			newGameType = GameType.valueOf(args[3]);
		}
		catch (Exception e)
		{
			getPlugin().sendValidGameTypes(player);
			return true;
		}

		String worldName = getPlugin().getWorldString(originalMapName, originalGametype);
		String newWorldName = getPlugin().getWorldString(newMapName, newGameType);

		if (!getPlugin().DoesMapExist(worldName))
		{
			message(player, "Could not find a map with the name " + F.elem(originalMapName) + " of type " + F.elem(originalGametype.toString()));
			return true;
		}

		if (getPlugin().DoesMapExist(newWorldName))
		{
			message(player, "Destination map already exists " + F.elem(newMapName) + " of type " + F.elem(newGameType.toString()));
			return true;
		}

		World world = getPlugin().GetMapWorld(worldName);

		if (world != null)
		{
			// World is loaded, save and unload it.

			for (Player other : world.getPlayers())
			{
				other.teleport(getPlugin().getSpawnLocation());
				message(other, "Unloading world for copy...");
			}
			Bukkit.getServer().unloadWorld( world, true);
		}

		File source = new File(worldName);
		File destination = new File(newWorldName);
		try
		{
			FileUtils.copyDirectory(source, destination);
			message(player, "Copy completed successfully!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			message(player, "An error occurred during map copy!");
		}

		return true;
	}
}
