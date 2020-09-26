package ehnetwork.hub.server.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.hub.server.ui.ServerGameMenu;

public class SelectUHCButton implements IButton
{
	private ServerGameMenu _menu;

	public SelectUHCButton(ServerGameMenu menu)
	{
		_menu = menu;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_menu.openUHC(player);
	}
}
