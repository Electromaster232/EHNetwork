package nautilus.game.arcade.shop;

import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import mineplex.core.account.CoreClientManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;

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
