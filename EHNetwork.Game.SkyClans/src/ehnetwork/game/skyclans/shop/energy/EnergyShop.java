package ehnetwork.game.skyclans.shop.energy;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.skyclans.clans.ClansManager;

public class EnergyShop extends ShopBase<ClansManager>
{

	public EnergyShop(ClansManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Energy Shop");
	}

	@Override
	protected ShopPageBase<ClansManager, ? extends ShopBase<ClansManager>> buildPagesFor(Player player)
	{
		return new EnergyPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
}
