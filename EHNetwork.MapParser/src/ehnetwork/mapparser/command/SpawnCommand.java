package ehnetwork.mapparser.command;

import org.bukkit.entity.Player;

import ehnetwork.mapparser.MapParser;

public class SpawnCommand extends BaseCommand
{

	public SpawnCommand(MapParser plugin)
	{
		super(plugin, "spawn");
	}

	@Override
	public boolean execute(Player player, String alias, String[] args)
	{
		player.teleport(player.getWorld().getSpawnLocation());

		return true;
	}

}
