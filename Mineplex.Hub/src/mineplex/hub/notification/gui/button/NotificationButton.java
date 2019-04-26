package mineplex.hub.notification.gui.button;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import mineplex.core.common.util.Callback;
import mineplex.core.shop.item.IButton;
import mineplex.hub.mail.MailManager;
import mineplex.hub.mail.MailMessage;
import mineplex.hub.notification.NotificationManager;
import mineplex.hub.notification.api.Notification;
import mineplex.hub.notification.gui.page.NotificationPage;

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
