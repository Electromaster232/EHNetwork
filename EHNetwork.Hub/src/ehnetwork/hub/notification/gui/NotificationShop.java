package ehnetwork.hub.notification.gui;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.hub.notification.NotificationManager;
import ehnetwork.hub.notification.gui.page.NotificationPage;

public class NotificationShop extends ShopBase<NotificationManager>
{
	public NotificationShop(NotificationManager plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super(plugin, clientManager, donationManager, "Notifications");
	}

	@Override
	protected ShopPageBase<NotificationManager, ? extends ShopBase<NotificationManager>> buildPagesFor(Player player)
	{
		return new NotificationPage(getPlugin(), this, getClientManager(), getDonationManager(), player);
	}
}
