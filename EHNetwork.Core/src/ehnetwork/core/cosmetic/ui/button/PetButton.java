package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.PetPage;
import ehnetwork.core.pet.Pet;
import ehnetwork.core.shop.item.IButton;

public class PetButton implements IButton
{
	private Pet _pet;
	private PetPage _page;
	
	public PetButton(Pet pet, PetPage page)
	{
		_pet = pet;
		_page = page;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_page.purchasePet(player, _pet);
	}
}
