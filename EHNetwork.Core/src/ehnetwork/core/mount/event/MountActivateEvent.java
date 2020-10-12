package ehnetwork.core.mount.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.core.mount.Mount;

public class MountActivateEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Player _player;
    private Mount<?> _mount;
    
    private boolean _cancelled = false;

    public MountActivateEvent(Player player, Mount<?> mount) 
    {
    	_player = player;
    	_mount = mount;
    }
  
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }

	public Mount getMount() 
	{
		return _mount;
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