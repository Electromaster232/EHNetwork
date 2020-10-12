package ehnetwork.game.arcade.game.games.skywars.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TNTPickupEvent extends PlayerEvent
{
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public HandlerList getHandlers()
	{
		return getHandlerList();
	}

	private static Player _who;

	public TNTPickupEvent(Player who)
	{
		super(who);
		_who = who;

	}

	public Player getWho()
	{
		return _who;
	}
}