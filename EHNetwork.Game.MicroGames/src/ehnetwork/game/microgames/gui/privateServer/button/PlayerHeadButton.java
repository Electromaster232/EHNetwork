package ehnetwork.game.microgames.gui.privateServer.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.page.MenuPage;

public class PlayerHeadButton implements IButton
{
	private MicroGamesManager _microGamesManager;
	private MenuPage _menuPage;

	public PlayerHeadButton(MicroGamesManager microGamesManager, MenuPage menuPage)
	{
		_microGamesManager = microGamesManager;
		_menuPage = menuPage;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		int maxPlayers = _microGamesManager.GetServerConfig().MaxPlayers;
		int newMax;

		int maxCap = _microGamesManager.GetGameHostManager().getMaxPlayerCap();

		if (clickType.isLeftClick())
			newMax = ++maxPlayers > maxCap ? maxCap : maxPlayers;
		else
			newMax = --maxPlayers < 2 ? 2 : maxPlayers;

		_microGamesManager.GetServerConfig().MaxPlayers = newMax;
		_menuPage.refresh();
	}
}
