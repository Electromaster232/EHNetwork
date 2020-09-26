package ehnetwork.core.treasure.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.treasure.TreasureLocation;
import ehnetwork.core.treasure.TreasureType;

public class OpenTreasureButton implements IButton
{

	private Player _player;
	private TreasureLocation _treasureLocation;
	private TreasureType _treasureType;

	public OpenTreasureButton(Player player, TreasureLocation treasureLocation, TreasureType treasureType)
	{
		_player = player;
		_treasureLocation = treasureLocation;
		_treasureType = treasureType;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_treasureLocation.attemptOpenTreasure(player, _treasureType);
		player.closeInventory();
	}
}
