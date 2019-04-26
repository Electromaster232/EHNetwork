package mineplex.core.gadget.event;

import mineplex.core.gadget.types.Gadget;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GadgetCollideEntityEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Gadget _gadget;
    private Entity _other;
    
    private boolean _cancelled = false;

    public GadgetCollideEntityEvent(Gadget gadget, Entity other) 
    {
    	_gadget = gadget;
    	_other = other;
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

	public Entity getOther()
	{
		return _other;
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