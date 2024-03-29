package ehnetwork.minecraft.game.classcombat.shop.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.shop.page.ArmorPage;

public class SelectClassButton implements IButton
{
	ArmorPage _page;
	private IPvpClass _pvpClass;
	
	public SelectClassButton(ArmorPage page, IPvpClass pvpClass)
	{
		_page = page;
		_pvpClass = pvpClass;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.SelectClass(player, _pvpClass);
	}
}
