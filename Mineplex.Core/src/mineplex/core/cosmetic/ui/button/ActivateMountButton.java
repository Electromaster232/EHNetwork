package mineplex.core.cosmetic.ui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.cosmetic.ui.page.MountPage;
import mineplex.core.mount.Mount;
import mineplex.core.shop.item.IButton;

public class ActivateMountButton implements IButton
{
	private Mount<?> _mount;
	private MountPage _page;
	
	public ActivateMountButton(Mount<?> mount, MountPage page)
	{
		_mount = mount;
		_page = page;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.playAcceptSound(player);
		_mount.Enable(player);
		_page.getShop().openPageForPlayer(player, new Menu(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), player));
	}
}
