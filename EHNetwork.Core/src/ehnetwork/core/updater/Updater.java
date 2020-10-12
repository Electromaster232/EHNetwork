package ehnetwork.core.updater;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.updater.event.UpdateEvent;

public class Updater implements Runnable 
{
	private JavaPlugin _plugin;
	
	public Updater(JavaPlugin plugin)
	{
		_plugin = plugin;
		_plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, this, 0L, 1L);
	}
	
	@Override
	public void run() 
	{
		for (UpdateType updateType : UpdateType.values())
		{
			if (updateType.Elapsed())
			{
				_plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
			}
		}
	}
}
