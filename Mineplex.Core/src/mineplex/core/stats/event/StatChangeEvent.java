package mineplex.core.stats.event;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StatChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    
    private String _player;
    private String _statName;
    private long _valueBefore;
    private long _valueAfter;
    
    public StatChangeEvent(String player, String statName, long valueBefore, long valueAfter)
    {
       _player = player;
       _statName = statName;
       _valueBefore = valueBefore;
       _valueAfter = valueAfter;
    }
 
    public HandlerList getHandlers()
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public String getPlayerName()
    {
    	return _player;
    }
    
    public String getStatName()
    {
    	return _statName;
    }
    
    public long getValueBefore()
    {
    	return _valueBefore;
    }
    
    public long getValueAfter()
    {
    	return _valueAfter;
    }
}
