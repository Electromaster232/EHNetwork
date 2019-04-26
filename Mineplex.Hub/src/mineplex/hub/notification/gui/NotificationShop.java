package mineplex.hub.notification.gui;

import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.notification.NotificationManager;
import mineplex.hub.notification.gui.page.NotificationPage;

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
