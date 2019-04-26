package mineplex.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.shop.item.IButton;

public class OpenCostumes implements IButton
{
	private Menu _menu;

	public OpenCostumes(Menu menu)
	{
		_menu = menu;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_menu.openCostumes(player);
	}
}
