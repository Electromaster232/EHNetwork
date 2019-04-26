package mineplex.minecraft.game.classcombat.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassCombatCreatureAllowSpawnEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    
    private boolean _allow;
    
    public ClassCombatCreatureAllowSpawnEvent(boolean allow)
    {
       _allow = allow;
    }
 
    public HandlerList getHandlers()
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public boolean getAllowed()
    {
    	return _allow;
    }
}
