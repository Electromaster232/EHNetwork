package mineplex.core.cosmetic.ui.button;

import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.cosmetic.ui.page.MountPage;
import mineplex.core.shop.item.IButton;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class OpenMounts implements IButton
{
	private Menu _page;
	
	public OpenMounts(Menu page)
	{
		_page = page;
	}
	
	public void onClick(Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new MountPage(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), "Mounts", player));
	}
}