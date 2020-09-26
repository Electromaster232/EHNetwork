package ehnetwork.game.arcade.shop;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.arcade.ArcadeManager;

public class ArcadeShop extends ShopBase<ArcadeManager>
{
	public ArcadeShop(ArcadeManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Shop", CurrencyType.Gems);
	}

	@Override
	protected ShopPageBase<ArcadeManager, ? extends ShopBase<ArcadeManager>> buildPagesFor(Player player)
	{
		return null;
	}
}
