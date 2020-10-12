package ehnetwork.minecraft.game.classcombat.shop.salespackage;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;
import ehnetwork.minecraft.game.classcombat.item.Item;

public class ItemSalesPackage extends SalesPackageBase
{
	public ItemSalesPackage(Item item)
	{
		super("Champions " + item.GetName(), Material.BOOK, (byte)0, item.GetDesc(), item.GetGemCost());
		Free = item.isFree();
		KnownPackage = false;
		CurrencyCostMap.put(CurrencyType.Gems, item.GetGemCost());
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{
		
	}
}
