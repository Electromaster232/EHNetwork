package mineplex.mapparser.command;

import org.bukkit.World;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class WorldsCommand extends BaseCommand
{
	public WorldsCommand(MapParser plugin)
	{
		super(plugin, "worlds");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		message(player, "Listing Active Worlds;");

		for (World world : getPlugin().getServer().getWorlds())
		{
			player.sendMessage(world.getName());
		}
		return true;
	}
}
