package mineplex.hub.server.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.server.ServerManager;

public class QuickShop extends ShopBase<ServerManager>
{
	public QuickShop(ServerManager plugin, CoreClientManager clientManager,	mineplex.core.donation.DonationManager donationManager,	String name)
	{
		super(plugin, clientManager, donationManager, name);
	}

	@Override
	protected ShopPageBase<ServerManager, ? extends ShopBase<ServerManager>> buildPagesFor(Player player)
	{
		return new ServerGameMenu(getPlugin(), this, getClientManager(), getDonationManager(), "          " + ChatColor.UNDERLINE + "Quick Game Menu", player);
	}

	public void UpdatePages()
	{
		for (ShopPageBase<ServerManager, ? extends ShopBase<ServerManager>> page : getPlayerPageMap().values())
		{
			if (page instanceof ServerGameMenu)
			{
				((ServerGameMenu)page).Update();
			}
		}
	}
}
