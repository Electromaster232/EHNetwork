package ehnetwork.game.skyclans.shop.pvp;

import org.bukkit.entity.Player;

import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.skyclans.clans.ClansManager;

public class PvpShop extends ShopBase<ClansManager>
{
	public PvpShop(ClansManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Pvp Gear");
	}

	@Override
	protected ShopPageBase<ClansManager, ? extends ShopBase<ClansManager>> buildPagesFor(Player player)
	{
		return new PvpPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
}
