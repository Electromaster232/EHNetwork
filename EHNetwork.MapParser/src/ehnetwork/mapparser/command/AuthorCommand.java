package ehnetwork.mapparser.command;

import org.bukkit.World;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.F;
import ehnetwork.mapparser.MapData;
import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class AuthorCommand extends BaseCommand
{
	public AuthorCommand(MapParser plugin)
	{
		super(plugin, "author");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		World world = player.getWorld();

		if (args.length < 1)
		{
			message(player, "Invalid Input. " + F.elem("/author <MapAuthor>"));
			return true;
		}

		String authorName = "";
		for (String arg : args)
			authorName += arg + " ";
		authorName = authorName.trim();

		if (world.getName().equals("world"))
		{
			message(player, "Cannot set author for Lobby.");
			return true;
		}

		//Permission
		if (!getPlugin().GetData(world.getName()).HasAccess(player))
		{
			message(player, "You do not have Build-Access on this Map.");
			return true;
		}

		MapData data = getPlugin().GetData(world.getName());

		data.MapCreator = authorName;
		data.Write();

		getPlugin().Announce("Map Author for " + F.elem(world.getName()) + " set to " + F.elem(authorName) + ".");

		return true;
	}
}
