package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.PrivateServerShop;
import ehnetwork.game.microgames.gui.privateServer.page.WhitelistedPage;

/**
 * Created by WilliamTiger.
 * All the code and any API's associated with it
 * are not to be used anywhere else without written
 * consent of William Burns. 2015.
 * 29/07/15
 */
public class WhitelistButton implements IButton
{
	private MicroGamesManager _microGamesManager;
	private PrivateServerShop _shop;

	public WhitelistButton(MicroGamesManager microGamesManager, PrivateServerShop shop)
	{
		_shop = shop;
		_microGamesManager = microGamesManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_shop.openPageForPlayer(player, new WhitelistedPage(_microGamesManager, _shop, player));
	}
}
