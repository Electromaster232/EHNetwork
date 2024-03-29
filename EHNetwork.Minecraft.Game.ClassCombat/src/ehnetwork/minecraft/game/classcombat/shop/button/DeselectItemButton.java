package ehnetwork.minecraft.game.classcombat.shop.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.minecraft.game.classcombat.item.Item;
import ehnetwork.minecraft.game.classcombat.shop.page.SkillPage;

public class DeselectItemButton implements IButton
{
	private SkillPage _page;
	private Item _item;
	private int _index;
	
	public DeselectItemButton(SkillPage page, Item item, int index)
	{
		_page = page;
		_item = item;
		_index = index;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.DeselectItem(player, _item, _index);
	}
}
