package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.GadgetPage;
import ehnetwork.core.cosmetic.ui.page.Menu;
import ehnetwork.core.shop.item.IButton;

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