package ehnetwork.core.punish.UI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.punish.Category;
import ehnetwork.core.shop.item.IButton;

public class PunishButton implements IButton
{
	private PunishPage _punishPage;
	private Category _category;
	private int _severity;
	private boolean _ban;
	private long _time;
	
	public PunishButton(PunishPage punishPage, Category category, int severity, boolean ban, long time)
	{
		_punishPage = punishPage;
		_category = category;
		_severity = severity;
		_ban = ban;
		_time = time;
	}

	public void onClick(Player player, ClickType clickType)
	{
		_punishPage.AddInfraction(_category, _severity, _ban, _time);
	}
}
