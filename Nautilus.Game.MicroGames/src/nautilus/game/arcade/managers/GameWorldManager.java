package nautilus.game.arcade.managers;

import java.util.HashSet;
import java.util.Iterator;

import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.world.WorldData;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class GameWorldManager implements Listener
{
	ArcadeManager Manager;
	 
	private HashSet<WorldData> _worldLoader = new HashSet<WorldData>();
	
	public GameWorldManager(ArcadeManager manager)
	{
		Manager = manager;
		
		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@EventHandler
	public void LoadWorldChunks(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
 
		Iterator<WorldData> worldIterator = _worldLoader.iterator();

		long endTime = System.currentTimeMillis() + 25;

		while (worldIterator.hasNext())
		{	
			long timeLeft = endTime - System.currentTimeMillis();
			if (timeLeft <= 0)	continue;

			final WorldData worldData = worldIterator.next();

			if (worldData.World == null)
			{
				worldIterator.remove();
			}
			else if (worldData.LoadChunks(timeLeft))
			{
				worldData.Host.SetState(GameState.Recruit);
				worldIterator.remove();
			}
		}
	}
	
	/*
	@EventHandler
	public void ChunkLoad(ChunkPreLoadEvent event)
	{
		if (Manager.GetGame() != null)
			if (Manager.GetGame().WorldData != null)
				Manager.GetGame().WorldData.ChunkLoad(event);
	}
	*/
	
	@EventHandler
	public void ChunkUnload(ChunkUnloadEvent event)
	{
		if (event.getWorld().getName().equals("world"))
		{
			event.setCancelled(true);
			return;
		}

		if (Manager.GetGame() != null)
			if (Manager.GetGame().WorldData != null)
				Manager.GetGame().WorldData.ChunkUnload(event);
	}

	public void RegisterWorld(WorldData worldData)
	{
		_worldLoader.add(worldData);
	}
}
