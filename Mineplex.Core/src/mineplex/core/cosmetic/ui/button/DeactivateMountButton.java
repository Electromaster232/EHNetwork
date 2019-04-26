package mineplex.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.shop.item.IButton;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.mount.Mount;

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
