package ehnetwork.game.microgames.gui.privateServer;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.gui.privateServer.page.MenuPage;

public class PrivateServerShop extends ShopBase<MicroGamesManager>
{
	public PrivateServerShop(MicroGamesManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Private Server Menu");
	}

	@Override
	protected ShopPageBase<MicroGamesManager, ? extends ShopBase<MicroGamesManager>> buildPagesFor(Player player)
	{
		return new MenuPage(getPlugin(), this, player);
	}
}
