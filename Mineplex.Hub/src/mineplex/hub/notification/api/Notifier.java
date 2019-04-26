package mineplex.hub.notification.api;

import java.util.List;

import org.bukkit.entity.Player;

public interface Notifier
{
	public List<? extends Notification> getNotifications(Player player);
}