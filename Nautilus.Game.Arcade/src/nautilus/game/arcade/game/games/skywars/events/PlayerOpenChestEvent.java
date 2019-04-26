package nautilus.game.arcade.game.games.skywars.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenChestEvent extends Event
{

	private static final HandlerList handlers = new HandlerList();
	
	private Player _who;
	
	public PlayerOpenChestEvent(Player who)
	{
		this._who = who;		
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public HandlerList getHandlers()
	{
		return getHandlerList();
	}

	public Player getPlayer() {
		
		return _who;
		
	}
	
}
