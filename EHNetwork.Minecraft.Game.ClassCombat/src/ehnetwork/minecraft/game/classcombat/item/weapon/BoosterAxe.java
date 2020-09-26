package ehnetwork.minecraft.game.classcombat.item.weapon;

import org.bukkit.Material;

import ehnetwork.minecraft.game.classcombat.item.Item;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;

public class BoosterAxe extends Item
{
	public BoosterAxe(ItemFactory factory, int gemCost, int tokenCost)
	{
		super(factory, "Booster Axe", new String [] { "Increases Axe Skill level by 1." }, Material.GOLD_AXE, 1, true, gemCost, tokenCost);
	}
}
