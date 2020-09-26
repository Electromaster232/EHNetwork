package ehnetwork.game.arcade.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.gui.privateServer.PrivateServerShop;
import ehnetwork.game.arcade.gui.privateServer.page.SetGamePage;

public class SetGameButton implements IButton
{
	private ArcadeManager _arcadeManager;
	private PrivateServerShop _shop;

	public SetGameButton(ArcadeManager arcadeManager, PrivateServerShop shop)
	{
		_shop = shop;
		_arcadeManager = arcadeManager;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_shop.openPageForPlayer(player, new SetGamePage(_arcadeManager, _shop, player));
	}
}
