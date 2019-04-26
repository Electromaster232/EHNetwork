package mineplex.core.gadget.event;

import mineplex.core.gadget.types.Gadget;
import mineplex.core.mount.Mount;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GadgetActivateEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Player _player;
    private Gadget _gadget;
    
    private boolean _cancelled = false;
    
    public GadgetActivateEvent(Player player, Gadget gadget) 
    {
    	_player = player;
    	_gadget = gadget;
    }
  
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }

	public Gadget getGadget() 
	{
		return _gadget;
	}

	public Player getPlayer()
	{
		return _player;
	}

	public void setCancelled(boolean cancel)
	{
		_cancelled = cancel;
	}
	
	public boolean isCancelled()
	{
		return _cancelled;
	}
}