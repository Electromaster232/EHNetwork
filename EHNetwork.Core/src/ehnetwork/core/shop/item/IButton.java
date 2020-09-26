package ehnetwork.core.shop.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface IButton
{
	public void onClick(Player player, ClickType clickType);
}
