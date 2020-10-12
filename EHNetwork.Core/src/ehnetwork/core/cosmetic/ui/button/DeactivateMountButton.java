package ehnetwork.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.mount.Mount;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.page.ShopPageBase;

public class DeactivateMountButton implements IButton
{
	private Mount<?> _mount;
	private ShopPageBase<?,?> _page;
	
	public DeactivateMountButton(Mount<?> mount, ShopPageBase<?,?> page)
	{
		_mount = mount;
		_page = page;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_page.playAcceptSound(player);
		_mount.Disable(player);
		_page.refresh();
	}
}
