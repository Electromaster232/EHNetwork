package ehnetwork.hub.notification.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.hub.notification.NotificationManager;
import ehnetwork.hub.notification.api.Notification;
import ehnetwork.hub.notification.gui.page.NotificationPage;

public class NotificationButton implements IButton
{
	private NotificationManager _manager;
	private NotificationPage _page;
	private Notification _notification;

	private Player _player;

	public NotificationButton(NotificationManager manager, Player player, NotificationPage page, Notification notification)
	{
		_manager = manager;
		_page = page;
		_notification = notification;
		_player = player;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_notification.clicked(player, clickType);
	}
}
