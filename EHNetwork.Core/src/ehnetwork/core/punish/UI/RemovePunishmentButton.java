package ehnetwork.core.punish.UI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.punish.Punishment;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;

public class RemovePunishmentButton implements IButton
{
	private PunishPage _punishPage;
	private Punishment _punishment;
	private ShopItem _item;
	
	public RemovePunishmentButton(PunishPage punishPage, Punishment punishment, ShopItem item)
	{
		_punishPage = punishPage;
		_punishment = punishment;
		_item = item;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_punishPage.RemovePunishment(_punishment, _item);
	}
}
