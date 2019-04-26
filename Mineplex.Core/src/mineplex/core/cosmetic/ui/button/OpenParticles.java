package mineplex.core.cosmetic.ui.button;

import mineplex.core.cosmetic.ui.page.Menu;
import mineplex.core.cosmetic.ui.page.ParticlePage;
import mineplex.core.shop.item.IButton;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class OpenParticles implements IButton
{
	private Menu _page;
	
	public OpenParticles(Menu page)
	{
		_page = page;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new ParticlePage(_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), "Particles", player));
	}
}