package nautilus.game.arcade.game.games.skywars.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillZombieEvent extends Event
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

	private final Player _who;
	private final Zombie _zombie;
	
	public PlayerKillZombieEvent(Player who, Zombie zombie)
	{
		this._who = who;
		this._zombie = zombie;

	}

	public Player getWho()
	{
		return _who;
	}
	
	public Zombie getZombie() {
		return _zombie;
	}
}
