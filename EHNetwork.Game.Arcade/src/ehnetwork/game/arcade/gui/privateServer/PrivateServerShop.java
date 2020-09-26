package ehnetwork.game.arcade.gui.privateServer;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.gui.privateServer.page.MenuPage;

public class PrivateServerShop extends ShopBase<ArcadeManager>
{
	public PrivateServerShop(ArcadeManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Private Server Menu");
	}

	@Override
	protected ShopPageBase<ArcadeManager, ? extends ShopBase<ArcadeManager>> buildPagesFor(Player player)
	{
		return new MenuPage(getPlugin(), this, player);
	}
}
