package ehnetwork.hub.notification.gui.page;

import java.util.List;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.hub.notification.NotificationManager;
import ehnetwork.hub.notification.api.Notification;
import ehnetwork.hub.notification.gui.NotificationShop;
import ehnetwork.hub.notification.gui.button.NotificationButton;

public class NotificationPage extends ShopPageBase<NotificationManager, NotificationShop>
{
	public NotificationPage(NotificationManager plugin, NotificationShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Notifications", player);

		refresh();
	}

	@Override
	protected void buildPage()
	{
		List<Notification> notifications = getPlugin().getNotifications(getPlayer());

		for (int i = 0; i < notifications.size(); i++)
		{
			Notification message = notifications.get(i);

			ShopItem item = getItem(message);

			addButton(i, item, new NotificationButton(getPlugin(), getPlayer(), this, message));
		}
	}

	private ShopItem getItem(Notification notification)
	{
		return new ShopItem(notification.getMaterial(), notification.getData(), notification.getTitle(), notification.getTitle(), notification.getText(), 1, false, false);
	}
}
