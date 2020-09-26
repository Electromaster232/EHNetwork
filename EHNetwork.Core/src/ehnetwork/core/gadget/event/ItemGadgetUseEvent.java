package ehnetwork.core.gadget.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.core.gadget.types.ItemGadget;

public class ItemGadgetUseEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Player _player;
    private ItemGadget _gadget;
    private int _count;
    
    private boolean _cancelled = false;
    
    public ItemGadgetUseEvent(Player player, ItemGadget gadget, int count) 
    {
    	_player = player;
    	_gadget = gadget;
    	_count = count;
    }
  
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }

	public int getCount() 
	{
		return _count;
	}
	
	public ItemGadget getGadget() 
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