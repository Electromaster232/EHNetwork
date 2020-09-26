package ehnetwork.game.microgames.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.GameTeam.PlayerState;

public class PlayerStateChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Game _game;
    private Player _player;
    private PlayerState _state;
    
    public PlayerStateChangeEvent(Game game, Player player, PlayerState state)
    {
       _game = game;
       _player = player;
       _state = state;
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
    
    public PlayerState GetState()
    {
    	return _state;
    }
}
