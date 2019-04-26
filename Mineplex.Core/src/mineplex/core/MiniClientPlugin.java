package mineplex.core;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.account.event.ClientUnloadEvent;
import mineplex.core.common.util.NautHashMap;

public abstract class MiniClientPlugin<DataType extends Object> extends MiniPlugin
{
	private static Object _clientDataLock = new Object();

	private NautHashMap<String, DataType> _clientData = new NautHashMap<String, DataType>();
	
	
	public MiniClientPlugin(String moduleName, JavaPlugin plugin) 
	{
		super(moduleName, plugin);
	}
	
	@EventHandler
	public void UnloadPlayer(ClientUnloadEvent event)
	{
		synchronized (_clientDataLock)
		{
			_clientData.remove(event.GetName());
		}
	}
	
	public DataType Get(String name)
	{
		synchronized (_clientDataLock)
		{
			if (!_clientData.containsKey(name))
				_clientData.put(name, AddPlayer(name));
			
			return _clientData.get(name);
		}
	}
	
	public DataType Get(Player player)
	{
		return Get(player.getName());
	}

	protected Collection<DataType> GetValues()
	{
		return _clientData.values();
	}
	
	protected void Set(Player player, DataType data)
	{
		Set(player.getName(), data);
	}
	
	protected void Set(String name, DataType data)
	{
		synchronized (_clientDataLock)
		{
			_clientData.put(name, data);
		}
	}
	
	protected abstract DataType AddPlayer(String player);
}
