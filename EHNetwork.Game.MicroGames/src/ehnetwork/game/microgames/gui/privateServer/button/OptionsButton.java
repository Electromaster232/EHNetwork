package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;
import ehnetwork.game.microgames.gui.privateServer.page.OptionsPage;

public class OptionsButton implements IButton
{
	private MicroGamesManager _microGamesManager;
	private PrivateServerShop _shop;

	public OptionsButton(MicroGamesManager microGamesManager, PrivateServerShop shop)
	{
		_shop = shop;
		_microGamesManager = microGamesManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_shop.openPageForPlayer(player, new OptionsPage(_microGamesManager, _shop, player));
	}
}
