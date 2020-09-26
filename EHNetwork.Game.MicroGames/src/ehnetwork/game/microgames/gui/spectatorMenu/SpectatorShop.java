package ehnetwork.game.microgames.gui.spectatorMenu;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.addons.CompassAddon;
import ehnetwork.game.microgames.gui.spectatorMenu.page.SpectatorPage;

/**
 * Created by shaun on 14-09-24.
 */
public class SpectatorShop extends ShopBase<CompassAddon>
{

	private MicroGamesManager _microGamesManager;

	public SpectatorShop(CompassAddon plugin, MicroGamesManager microGamesManager, CoreClientManager clientManager, DonationManager donationManager, CurrencyType... currencyTypes)
	{
		super(plugin, clientManager, donationManager, "Spectate Menu", currencyTypes);
		_microGamesManager = microGamesManager;
	}

	@Override
	protected ShopPageBase<CompassAddon, ? extends ShopBase<CompassAddon>> buildPagesFor(Player player)
	{
		return new SpectatorPage(getPlugin(), _microGamesManager, this, getClientManager(), getDonationManager(), player);
	}

	public void update()
	{
		for (ShopPageBase<CompassAddon, ? extends ShopBase<CompassAddon>> shopPage : getPlayerPageMap().values())
		{
			shopPage.refresh();
		}
	}

}