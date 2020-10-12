package ehnetwork.game.arcade.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.kit.Kit;

public class PlayerKitGiveEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Game _game;
    private Kit _kit;
    private Player _player;
    
    public PlayerKitGiveEvent(Game game, Kit kit, Player player)
    {
       _game = game;
       _kit = kit;
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
        
    public Kit GetKit()
    {
    	return _kit;
    }
    
    public Player GetPlayer()
    {
    	return _player;
    }
}
