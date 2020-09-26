package ehnetwork.game.arcade.gui.spectatorMenu;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.addons.CompassAddon;
import ehnetwork.game.arcade.gui.spectatorMenu.page.SpectatorPage;

/**
 * Created by shaun on 14-09-24.
 */
public class SpectatorShop extends ShopBase<CompassAddon>
{

	private ArcadeManager _arcadeManager;

	public SpectatorShop(CompassAddon plugin, ArcadeManager arcadeManager, CoreClientManager clientManager, DonationManager donationManager, CurrencyType... currencyTypes)
	{
		super(plugin, clientManager, donationManager, "Spectate Menu", currencyTypes);
		_arcadeManager = arcadeManager;
	}

	@Override
	protected ShopPageBase<CompassAddon, ? extends ShopBase<CompassAddon>> buildPagesFor(Player player)
	{
		return new SpectatorPage(getPlugin(), _arcadeManager, this, getClientManager(), getDonationManager(), player);
	}

	public void update()
	{
		for (ShopPageBase<CompassAddon, ? extends ShopBase<CompassAddon>> shopPage : getPlayerPageMap().values())
		{
			shopPage.refresh();
		}
	}

}