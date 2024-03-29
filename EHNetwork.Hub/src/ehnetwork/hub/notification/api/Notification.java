package ehnetwork.hub.notification.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface Notification
{
	public String getTitle();

	public String[] getText();

	public long getTime();

	public Material getMaterial();

	public byte getData();

	public void clicked(Player player, ClickType clickType);

	public NotificationPriority getPriority();
}
