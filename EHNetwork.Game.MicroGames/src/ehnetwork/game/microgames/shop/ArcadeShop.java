package ehnetwork.game.microgames.shop;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.microgames.MicroGamesManager;

public class ArcadeShop extends ShopBase<MicroGamesManager>
{
	public ArcadeShop(MicroGamesManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Shop", CurrencyType.Gems);
	}

	@Override
	protected ShopPageBase<MicroGamesManager, ? extends ShopBase<MicroGamesManager>> buildPagesFor(Player player)
	{
		return null;
	}
}
