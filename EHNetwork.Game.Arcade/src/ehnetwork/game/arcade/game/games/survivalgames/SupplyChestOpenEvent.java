package ehnetwork.game.arcade.game.games.survivalgames;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SupplyChestOpenEvent extends PlayerEvent
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

	private final Block _chest;

	public SupplyChestOpenEvent(Player who, Block chest)
	{
		super(who);

		_chest = chest;
	}

	public Block getChest()
	{
		return _chest;
	}
}
