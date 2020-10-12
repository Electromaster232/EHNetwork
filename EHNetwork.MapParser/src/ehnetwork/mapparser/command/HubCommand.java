package ehnetwork.mapparser.command;

import org.bukkit.entity.Player;

import ehnetwork.mapparser.MapParser;

/**
 * Created by Shaun on 8/15/2014.
 */
public class HubCommand extends BaseCommand
{
	public HubCommand(MapParser plugin)
	{
		super(plugin, "hub");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		player.teleport(getPlugin().getSpawnLocation());
		return true;
	}
}
