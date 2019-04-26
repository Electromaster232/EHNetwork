package mineplex.core.mount;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mineplex.core.common.CurrencyType;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.mount.event.MountActivateEvent;
import mineplex.core.shop.item.SalesPackageBase;

public abstract class Mount<T> extends SalesPackageBase implements Listener
{
	protected HashSet<Player> _owners = new HashSet<Player>();
	protected HashMap<Player, T> _active = new HashMap<Player, T>();
	
	public MountManager Manager;
	
	public Mount(MountManager manager, String name, Material material, byte displayData, String[] description, int coins) 
	{
		super(name, material, displayData, description, coins);
		
		Manager = manager;
		
		Manager.getPlugin().getServer().getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType) 
	{
		
	}
	
	public final void Enable(Player player)
	{
		MountActivateEvent gadgetEvent = new MountActivateEvent(player, this);		
		Bukkit.getServer().getPluginManager().callEvent(gadgetEvent);
		
		if (gadgetEvent.isCancelled())
		{
			UtilPlayer.message(player, F.main("Inventory", GetName() + " is not enabled."));
			return;
		}
		
		Manager.setActive(player, this);
		EnableCustom(player);
	}
	public abstract void EnableCustom(Player player);
	public abstract void Disable(Player player);
	
	public void DisableForAll() 
	{
		for (Player player : UtilServer.getPlayers())
			Disable(player);
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		if (event.getPlayer().isOp())
			_owners.add(event.getPlayer());
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		_owners.remove(event.getPlayer());
		Disable(event.getPlayer());
	}
	
	public HashSet<Player> GetOwners()
	{
		return _owners;
	}
	
	public HashMap<Player, T> GetActive()
	{
		return _active;
	}
	
	public boolean IsActive(Player player)
	{
		return _active.containsKey(player);
	}
	
	public boolean HasMount(Player player)
	{
		return _owners.contains(player);
	}
}
