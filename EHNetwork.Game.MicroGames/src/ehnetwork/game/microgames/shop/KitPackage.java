package ehnetwork.game.microgames.shop;

import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;
import ehnetwork.game.microgames.kit.Kit;

public class KitPackage extends SalesPackageBase
{
	public KitPackage(String gameName, Kit kit)
	{
		super(gameName + " " + kit.GetName(), kit.getDisplayMaterial(), kit.GetDesc());
		KnownPackage = false;
		CurrencyCostMap.put(CurrencyType.Gems, kit.GetCost());
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{

	}
}
