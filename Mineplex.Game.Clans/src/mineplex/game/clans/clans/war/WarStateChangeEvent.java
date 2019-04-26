package mineplex.game.clans.clans.war;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarStateChangeEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	private WarState _oldState;
	private WarState _newState;

	public WarStateChangeEvent(WarState oldState, WarState newState)
	{
		_oldState = oldState;
		_newState = newState;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	public WarState getOldState()
	{
		return _oldState;
	}

	public WarState getNewState()
	{
		return _newState;
	}

}