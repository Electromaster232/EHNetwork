package mineplex.core.spawn;

import java.util.ArrayList;
import java.util.List;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.spawn.command.SpawnCommand;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn extends MiniPlugin
{
	private SpawnRepository _repository;
	
	private List<Location> _spawns = new ArrayList<Location>();

	public Spawn(JavaPlugin plugin, String serverName) 
	{
		super("Spawn", plugin);
	
		_repository = new SpawnRepository(plugin, serverName);
		
		for (String spawn : _repository.retrieveSpawns())
		{
			_spawns.add(UtilWorld.strToLoc(spawn));
		}
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new SpawnCommand(this));
	}

	public Location getSpawn() 
	{
		if (_spawns.isEmpty())
			return UtilServer.getServer().getWorld("world").getSpawnLocation();

		return _spawns.get(UtilMath.r(_spawns.size()));
	}

	public void AddSpawn(Player player)
	{
		//Set Spawn Point
		final Location loc = player.getLocation();

		//Set World Spawn
		player.getWorld().setSpawnLocation((int)loc.getX(), (int)loc.getY(), (int)loc.getZ());

		//Add Spawn
		_spawns.add(loc);
		
		//Save
		runAsync(new Runnable()
		{
			public void run()
			{
				_repository.addSpawn(UtilWorld.locToStr(loc));
			}
		});
		
		//Inform
		UtilPlayer.message(player, F.main(_moduleName, "You added a Spawn Node."));

		//Log
		log("Added Spawn [" + UtilWorld.locToStr(loc) + "] by [" + player.getName() + "].");
	}

	public void ClearSpawn(Player player)
	{
		//Add Spawn
		_spawns.clear();

		//Save
		runAsync(new Runnable()
		{
			public void run()
			{
				_repository.clearSpawns();
			}
		});

		//Inform
		UtilPlayer.message(player, F.main(_moduleName, "You cleared all Spawn Nodes."));

		//Log
		log("Cleared Spawn [ALL] by [" + player.getName() + "].");
	}

	@EventHandler
	public void handleRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(getSpawn());
	}
}
