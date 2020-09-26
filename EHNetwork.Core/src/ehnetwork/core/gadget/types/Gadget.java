package ehnetwork.core.gadget.types;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.event.GadgetActivateEvent;
import ehnetwork.core.shop.item.SalesPackageBase;

public abstract class Gadget extends SalesPackageBase implements Listener
{
	public GadgetManager Manager;
	
	private GadgetType _gadgetType;
	
	protected HashSet<Player> _active = new HashSet<Player>();
	
	public Gadget(GadgetManager manager, GadgetType gadgetType, String name, String[] desc, int cost, Material mat, byte data) 
	{
		this(manager, gadgetType, name, desc, cost, mat, data, 1);
	}
	
	public Gadget(GadgetManager manager, GadgetType gadgetType, String name, String[] desc, int cost, Material mat, byte data, int quantity) 
	{
		super(name, mat, data, desc, cost, quantity);
		
		_gadgetType = gadgetType;
		KnownPackage = false;
		
		Manager = manager;
		
		Manager.getPlugin().getServer().getPluginManager().registerEvents(this, Manager.getPlugin());
	}
	
	public GadgetType getGadgetType()
	{
		return _gadgetType;
	}
	
	public HashSet<Player> GetActive()
	{
		return _active;
	}
	
	public boolean IsActive(Player player)
	{
		return _active.contains(player);
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Disable(event.getPlayer());
	}
	
	public void Enable(Player player)
	{
		GadgetActivateEvent gadgetEvent = new GadgetActivateEvent(player, this);		
		Bukkit.getServer().getPluginManager().callEvent(gadgetEvent);
		
		if (gadgetEvent.isCancelled())
		{
			UtilPlayer.message(player, F.main("Inventory", GetName() + " is not enabled."));
			return;
		}
		
		EnableCustom(player);
		Manager.setActive(player, this);
	}
	
	public void DisableForAll() 
	{
		for (Player player : UtilServer.getPlayers())
			Disable(player);
	}
	
	public void Disable(Player player)
	{
		if (IsActive(player))
			Manager.removeActive(player, this);
		
		DisableCustom(player);
	}
	
	public abstract void EnableCustom(Player player);
	public abstract void DisableCustom(Player player);
	
	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{

	}

	
}
