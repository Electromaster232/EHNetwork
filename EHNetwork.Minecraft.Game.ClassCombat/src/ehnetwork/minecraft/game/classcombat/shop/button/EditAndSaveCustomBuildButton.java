package ehnetwork.minecraft.game.classcombat.shop.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.CustomBuildToken;
import ehnetwork.minecraft.game.classcombat.shop.page.CustomBuildPage;

public class EditAndSaveCustomBuildButton implements IButton
{
	private CustomBuildPage _page;
	private CustomBuildToken _customBuild;
	
	public EditAndSaveCustomBuildButton(CustomBuildPage page, CustomBuildToken customBuild)
	{
		_page = page;
		_customBuild = customBuild;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.EditAndSaveCustomBuild(_customBuild);
	}
}
