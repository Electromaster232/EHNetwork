package ehnetwork.core.treasure;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;

public class ChestPackage extends SalesPackageBase
{
	public ChestPackage(String name, Material mat, int cost)
	{
		super(name, mat, (byte) 0, new String[] {}, cost);
 
		KnownPackage = false;
		OneTimePurchaseOnly = false;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{

	}
}
