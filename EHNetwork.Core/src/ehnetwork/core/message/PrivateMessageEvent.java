package ehnetwork.core.message;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrivateMessageEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private boolean _cancelled = false;
    private Player _sender;
    private Player _recipient;
    private String _msg;
    
    public PrivateMessageEvent(Player sender, Player recipient, String msg) 
    {
    	_sender = sender;
    	_recipient = recipient;
    	_msg = msg;
    }
  
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }

	public void setCancelled(boolean cancel)
	{
		_cancelled = cancel;
	}
	
	public boolean isCancelled()
	{
		return _cancelled;
	}
	
	public Player getSender()
	{
		return _sender;
	}
	
	public Player getRecipient()
	{
		return _recipient;
	}
	
	public String getMessage()
	{
		return _msg;
	}
}