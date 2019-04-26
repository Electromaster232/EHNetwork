package mineplex.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.shop.item.IButton;

/**
 * Created by shaun on 14-09-15.
 */
public class OpenMusic implements IButton
{
	private Menu _menu;

	public OpenMusic(Menu menu)
	{
		_menu = menu;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_menu.openMusic(player);
	}
}
