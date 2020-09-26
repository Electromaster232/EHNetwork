package ehnetwork.minecraft.game.classcombat.shop.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.shop.item.IButton;
import ehnetwork.minecraft.game.classcombat.Skill.ISkill;
import ehnetwork.minecraft.game.classcombat.shop.page.SkillPage;

public class PurchaseSkillButton implements IButton
{
	private SkillPage _page;
	private ISkill _skill;
	
	public PurchaseSkillButton(SkillPage page, ISkill skill)
	{
		_page = page;
		_skill = skill;
	}

	@Override
	public void onClick(Player player, ClickType clickType)
	{
		_page.PurchaseSkill(player, _skill);
	}
}
