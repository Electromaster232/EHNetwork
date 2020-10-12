package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.PetTagPage;
import ehnetwork.core.shop.item.IButton;

public class SelectTagButton implements IButton
{
	private PetTagPage _page;
	
	public SelectTagButton(PetTagPage page)
	{
		_page = page;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_page.SelectTag();
	}
}
