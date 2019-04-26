package mineplex.hub.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.jsonchat.ClickEvent;
import mineplex.core.common.jsonchat.Color;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.donation.DonationManager;
import mineplex.hub.notification.api.Notification;
import mineplex.hub.notification.api.Notifier;
import mineplex.hub.notification.command.NotificationCommand;
import mineplex.hub.notification.gui.NotificationShop;

public class NotificationManager extends MiniPlugin
{
	private static NotificationComparator COMPARATOR = new NotificationComparator();

	private NotificationShop _shop;
	private List<Notifier> _notifiers;

	public NotificationManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Notification", plugin);

		_notifiers = new ArrayList<Notifier>();
		_shop = new NotificationShop(this, clientManager, donationManager);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		runSyncLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (player.isOnline())
				{
					if (getNotifications(player).size() > 0)
					{
						JsonMessage message = new JsonMessage("You have unread notifications. Click here to read them!");
						message.color(Color.RED).click(ClickEvent.RUN_COMMAND, "/notifications");
						message.send(JsonMessage.MessageType.SYSTEM_MESSAGE, player);
					}
				}
			}
		}, 20 * 8);
	}

	public List<Notification> getNotifications(Player player)
	{
		List<Notification> notifications = new ArrayList<Notification>();

		for (Notifier notifier : _notifiers)
		{
			notifications.addAll(notifier.getNotifications(player));
		}

		Collections.sort(notifications, COMPARATOR);

		return notifications;
	}

	public void addNotifier(Notifier notifier)
	{
		_notifiers.add(notifier);
	}

	public void clearNotifiers()
	{
		_notifiers.clear();
	}

	public void openShop(Player caller)
	{
		_shop.attemptShopOpen(caller);
	}

	@Override
	public void addCommands()
	{
		addCommand(new NotificationCommand(this));
	}

	private static class NotificationComparator implements Comparator<Notification>
	{
		@Override
		public int compare(Notification n1, Notification n2)
		{
			if (n1.getPriority() == n2.getPriority())
			{
				return (int) (n1.getTime() - n2.getTime());
			}
			else
			{
				return n1.getPriority().ordinal() - n2.getPriority().ordinal();
			}
		}

	}
}
