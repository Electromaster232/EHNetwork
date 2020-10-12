package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.cosmetic.ui.page.GadgetPage;
import ehnetwork.core.gadget.types.Gadget;
import ehnetwork.core.shop.item.IButton;

public class DeactivateGadgetButton implements IButton
{
	private Gadget _gadget;
	private GadgetPage _page;
	
	public DeactivateGadgetButton(Gadget gadget, GadgetPage page)
	{
		_gadget = gadget;
		_page = page;
	}
	
	public void onClick(Player player, ClickType clickType)
	{
		if (clickType.isLeftClick())
			_page.deactivateGadget(player, _gadget);
		else if (clickType.isRightClick())
			_page.handleRightClick(player, _gadget);

	}
}
