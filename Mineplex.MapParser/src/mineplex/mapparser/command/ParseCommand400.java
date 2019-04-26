package mineplex.mapparser.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.mapparser.MapData;
import mineplex.mapparser.MapParser;
import mineplex.mapparser.Parse;

/**
 * Created by Shaun on 8/15/2014.
 */
public class ParseCommand400 extends BaseCommand
{
	public ParseCommand400(MapParser plugin)
	{
		super(plugin, "parse400");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		if (!player.isOp())
		{
			message(player, "Only OPs can parse maps!");
			return true;
		}
		
		Location parseLoc = player.getLocation();

		World world = parseLoc.getWorld();

		MapData data = getPlugin().GetData(world.getName());

		if (data.MapName.equals("null") || data.MapCreator.equals("null") || data.MapGameType.equals("null"))
		{
			message(player, "Map Name/Author/GameType are not set!");
			return true;
		}

		//Teleport Players Out
		for (Player worldPlayer : world.getPlayers())
		{
			worldPlayer.teleport(getPlugin().getSpawnLocation());
			message(player, "World " + F.elem(world.getName()) + " is preparing to be parsed.");
		}

		//Unload World > Copy
		World parseableWorld = getPlugin().getWorldManager().prepMapParse(world);

		if (parseableWorld == null)
		{
			message(player, "Could not prepare world for parsing!");
			return true;
		}

		//Parse the World
		getPlugin().setCurrentParse(new Parse(getPlugin(), parseableWorld, args, parseLoc, getPlugin().GetData(parseLoc.getWorld().getName()), 400));

		return true;
	}
}
