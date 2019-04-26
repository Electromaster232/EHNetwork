package mineplex.core.punish.UI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.punish.Punishment;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.item.IButton;

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
