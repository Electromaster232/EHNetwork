package mineplex.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.cosmetic.ui.page.GadgetPage;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.shop.item.IButton;

public class ActivateGadgetButton implements IButton
{
	private Gadget _gadget;
	private GadgetPage _page;
	
	public ActivateGadgetButton(Gadget gadget, GadgetPage page)
	{
		_gadget = gadget;
		_page = page;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		if (clickType.isLeftClick())
			_page.activateGadget(player, _gadget);
		else if (clickType.isRightClick())
			_page.handleRightClick(player, _gadget);
	}
}
