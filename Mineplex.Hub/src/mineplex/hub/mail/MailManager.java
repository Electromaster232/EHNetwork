package mineplex.hub.mail;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniClientPlugin;
import mineplex.core.common.util.Callback;
import mineplex.hub.notification.NotificationManager;
import mineplex.hub.notification.api.Notification;
import mineplex.hub.notification.api.Notifier;

public class MailManager extends MiniClientPlugin<PlayerMailData> implements Notifier
{
	private MailRepository _repository;

	public MailManager(JavaPlugin plugin, NotificationManager notificationManager)
	{
		super("Mail", plugin);

		_repository = new MailRepository(plugin, this);

		notificationManager.addNotifier(this);
	}

	@Override
	protected PlayerMailData AddPlayer(String player)
	{
		return new PlayerMailData();
	}

	@EventHandler
	protected void loadPlayerData(final PlayerJoinEvent event)
	{
		runAsync(new Runnable()
		{
			@Override
			public void run()
			{
				Set(event.getPlayer().getName(), _repository.loadMailData(event.getPlayer().getUniqueId()));
			}
		});
	}

	public void archive(final MailMessage message, final Callback<Boolean> callback)
	{
		if (message.isArchived())
			return;

		runAsync(new Runnable()
		{
			@Override
			public void run()
			{
				final boolean completed = _repository.archive(message);
				runSync(new Runnable()
				{
					@Override
					public void run()
					{
						callback.run(completed);
					}
				});
			}
		});
	}

	@Override
	public List<? extends Notification> getNotifications(Player player)
	{
		return Get(player).getUnreadMessages();
	}
}
