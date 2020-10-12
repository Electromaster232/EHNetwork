package ehnetwork.game.microgames.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.Game.GameState;

public class GameStateChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Game _game;
    private GameState _to;
    
    public GameStateChangeEvent(Game game, GameState to)
    {
       _game = game;
       _to = to;
    }
 
    public HandlerList getHandlers()
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public Game GetGame()
    {
    	return _game;
    }
        
    public GameState GetState()
    {
    	return _to;
    }
}
