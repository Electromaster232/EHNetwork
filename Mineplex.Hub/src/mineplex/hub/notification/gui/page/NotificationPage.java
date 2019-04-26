package mineplex.hub.notification.gui.page;

import java.util.List;

import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.notification.NotificationManager;
import mineplex.hub.notification.api.Notification;
import mineplex.hub.notification.gui.NotificationShop;
import mineplex.hub.notification.gui.button.NotificationButton;

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
