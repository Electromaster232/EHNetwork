package ehnetwork.game.arcade.managers;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.mineplex.spigot.ChunkPreLoadEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.world.WorldData;
import static org.bukkit.event.EventPriority.HIGHEST;

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
	

	@EventHandler
	public void ChunkLoad(ChunkPreLoadEvent event)
	{
		if (Manager.GetGame() != null)
			if (Manager.GetGame().WorldData != null)
				Manager.GetGame().WorldData.ChunkLoad(event);
	}


	@EventHandler
	public void ChunkUnload(ChunkUnloadEvent event)
	{
		//event.setCancelled(true);
		if (event.getWorld().getName().equals("world"))
		{
			event.setCancelled(true);
			return;
		}

		if (Manager.GetGame() != null)
		{
			if (Manager.GetGame().WorldData != null)
			{

				//if (Manager.GetGame().WorldChunkUnload)
				//{
				//	return;
				//}
				if (Manager.GetGame().WorldData.World == null)
					return;

				if (!event.getWorld().equals(Manager.GetGame().WorldData.World))
					return;

				event.setCancelled(true);
			}
		}
	}



	public void RegisterWorld(WorldData worldData)
	{
		_worldLoader.add(worldData);
	}
}
