package mineplex.core;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import mineplex.core.command.CommandCenter;
import mineplex.core.command.ICommand;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;

public abstract class MiniPlugin implements Listener 
{
	protected String _moduleName = "Default";
	protected JavaPlugin _plugin;
	protected NautHashMap<String, ICommand> _commands;
	
	public MiniPlugin(String moduleName, JavaPlugin plugin) 
	{
        _moduleName = moduleName;
        _plugin = plugin;
        
        _commands = new NautHashMap<String, ICommand>();
        
        onEnable();
        
        registerEvents(this);
	}
	
	public PluginManager getPluginManager()
	{
		return _plugin.getServer().getPluginManager();
	}
	
	public BukkitScheduler getScheduler()
	{
		return _plugin.getServer().getScheduler();
	}
	
	public JavaPlugin getPlugin()
	{
		return _plugin;
	}
	
	public void registerEvents(Listener listener)
	{
		_plugin.getServer().getPluginManager().registerEvents(listener, _plugin);
	}
	
	public void registerSelf()
	{
		registerEvents(this);
	}
	
	public void deregisterSelf()
	{
		HandlerList.unregisterAll(this);
	}
	
	public final void onEnable()
	{
		long epoch = System.currentTimeMillis();
		log("Initializing...");
		enable();
		addCommands();
		log("Enabled in " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".");
	}

	public final void onDisable()
	{
		disable();
		
		log("Disabled.");
	}

	public void enable() { }
	
	public void disable() { }
	
	public void addCommands() { }
	
	public final String getName()
	{
		return _moduleName;
	}
	
	public final void addCommand(ICommand command)
	{
		CommandCenter.Instance.AddCommand(command);
	}
	
	public final void removeCommand(ICommand command)
	{
		CommandCenter.Instance.RemoveCommand(command);
	}

	public void log(String message)
	{
		System.out.println(F.main(_moduleName, message));
	}
	
	public void runAsync(Runnable runnable)
	{
		_plugin.getServer().getScheduler().runTaskAsynchronously(_plugin, runnable);
	}

	public void runSync(Runnable runnable)
	{
		_plugin.getServer().getScheduler().runTask(_plugin, runnable);
	}

	public void runSyncLater(Runnable runnable, long delay)
	{
		_plugin.getServer().getScheduler().runTaskLater(_plugin, runnable, delay);
	}
}
