package ehnetwork.minecraft.game.classcombat.shop.salespackage;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;
import ehnetwork.minecraft.game.classcombat.Skill.ISkill;

public class SkillSalesPackage extends SalesPackageBase
{
	public SkillSalesPackage(ISkill skill)
	{
		super("Champions " + skill.GetName(), Material.BOOK, (byte)0, skill.GetDesc(0), skill.GetGemCost());
		Free = skill.IsFree();
		KnownPackage = false;
		CurrencyCostMap.put(CurrencyType.Gems, skill.GetGemCost());
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{
		
	}
}
