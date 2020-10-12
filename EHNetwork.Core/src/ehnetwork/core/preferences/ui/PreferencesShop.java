package ehnetwork.core.preferences.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;

public class PreferencesShop extends ShopBase<PreferencesManager>
{
	public PreferencesShop(PreferencesManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "User Preferences");
	}

	@Override
	protected ShopPageBase<PreferencesManager, ? extends ShopBase<PreferencesManager>> buildPagesFor(Player player)
	{
		return new PreferencesPage(getPlugin(), this, getClientManager(), getDonationManager(), "          " + ChatColor.UNDERLINE + "User Preferences", player);
	}
}