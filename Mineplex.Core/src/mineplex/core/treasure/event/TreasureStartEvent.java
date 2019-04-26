package mineplex.core.treasure.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by shaun on 14-09-12.
 */
public class TreasureStartEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();

	private Player _player;
	private boolean _cancelled = false;

	public TreasureStartEvent(Player player)
	{
		_player = player;
	}

	public Player getPlayer()
	{
		return _player;
	}

	@Override
	public boolean isCancelled()
	{
		return _cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled)
	{
		_cancelled = cancelled;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}