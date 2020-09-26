package ehnetwork.mapparser.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.mapparser.GameType;
import ehnetwork.mapparser.MapData;
import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/16/2014.
 */
public class CreateCommand extends BaseCommand
{
	public CreateCommand(MapParser plugin)
	{
		super(plugin, "create");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (args.length < 1)
		{
			message(player, "Invalid Input. " + F.elem("/create <MapName>"));
			return true;
		}

		GameType gameType = GameType.InProgress;

		String worldName = "map/" + gameType.GetName() + "/" + args[0];

		if (getPlugin().DoesMapExist(worldName))
		{
			message(player, "Map name is already in use!");
			return true;
		}

		getPlugin().Announce("Creating World: " + F.elem(worldName));

		WorldCreator worldCreator = new WorldCreator(worldName);
		worldCreator.environment(World.Environment.NORMAL);
		worldCreator.type(WorldType.FLAT);
		worldCreator.generateStructures(false);

		World world = Bukkit.getServer().createWorld(worldCreator);
		
		world.setSpawnLocation(0, 100, 0);

		message(player, "Teleporting to World: " + F.elem(worldName));

		player.teleport(world.getSpawnLocation());

		//Give Access
		MapData mapData = getPlugin().GetData(worldName);
		mapData.AdminList.add(player.getName());
		mapData.MapGameType = gameType;
		mapData.Write();

		return true;
	}
}
