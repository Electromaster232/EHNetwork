package ehnetwork.core.cosmetic.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ActivateGemBoosterEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Player _player;
    
    private boolean _cancelled = false;

    public ActivateGemBoosterEvent(Player player) 
    {
    	_player = player;
    }
  
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
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