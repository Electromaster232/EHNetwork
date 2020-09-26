package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.Menu;
import ehnetwork.core.cosmetic.ui.page.PetPage;
import ehnetwork.core.shop.item.IButton;

public class OpenPets implements IButton
{
	private Menu _page;
	
	public OpenPets(Menu page)
	{
		_page = page;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new PetPage(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), "Pets", player));
	}
}