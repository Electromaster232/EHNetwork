package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;

public class Ammo extends SalesPackageBase
{
	public Ammo(String name, String displayName, Material material, byte displayData, String[] description, int coins, int quantity)
	{
		super(name, material, displayData, description, coins, quantity);
		
		DisplayName = displayName;
		KnownPackage = false;
		OneTimePurchaseOnly = false;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{
	}
}
