package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.Menu;
import ehnetwork.core.cosmetic.ui.page.MorphPage;
import ehnetwork.core.shop.item.IButton;

public class OpenMorphs implements IButton
{
	private Menu _page;
	
	public OpenMorphs(Menu page)
	{
		_page = page;
	}
	
	public void onClick(Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new MorphPage(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), "Morphs", player));
	}
}