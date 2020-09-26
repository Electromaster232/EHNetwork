package ehnetwork.core.gadget.event;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.core.gadget.types.Gadget;

public class GadgetBlockEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Gadget _gadget;
    private List<Block> _blocks;
    
    private boolean _cancelled = false;

    public GadgetBlockEvent(Gadget gadget, List<Block> blocks) 
    {
    	_gadget = gadget;
    	_blocks = blocks;
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

	public List<Block> getBlocks()
	{
		return _blocks;
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