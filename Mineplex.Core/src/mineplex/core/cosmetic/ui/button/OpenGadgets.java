package mineplex.core.cosmetic.ui.button;

import mineplex.core.cosmetic.ui.page.GadgetPage;
import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.shop.item.IButton;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class OpenGadgets implements IButton
{
	private Menu _page;
	
	public OpenGadgets(Menu page)
	{
		_page = page;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new GadgetPage(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), "Gadgets", player));
	}
}