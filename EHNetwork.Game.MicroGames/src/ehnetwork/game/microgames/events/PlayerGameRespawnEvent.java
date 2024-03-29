package ehnetwork.game.microgames.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.game.microgames.game.Game;

public class PlayerGameRespawnEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Game _game;
    private Player _player;
    
    public PlayerGameRespawnEvent(Game game, Player player)
    {
       _game = game;
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
    
    public Game GetGame()
    {
    	return _game;
    }
    
    public Player GetPlayer()
    {
    	return _player;
    }
}
