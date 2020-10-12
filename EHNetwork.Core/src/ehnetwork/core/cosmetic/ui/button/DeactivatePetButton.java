package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.pet.PetManager;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.page.ShopPageBase;

public class DeactivatePetButton implements IButton
{
	private ShopPageBase<?,?> _page;
	private PetManager _petManager;
	
	public DeactivatePetButton(ShopPageBase<?,?> page, PetManager petManager)
	{
		_page = page;
		_petManager = petManager;
	}
	
	public void onClick(Player player, ClickType clickType)
	{
		_page.playAcceptSound(player);
		_petManager.RemovePet(player, true);
		_page.refresh();
	}
}
