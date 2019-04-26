package nautilus.game.arcade.shop;

import org.bukkit.entity.Player;

import nautilus.game.arcade.kit.Kit;
import mineplex.core.common.CurrencyType;
import mineplex.core.shop.item.SalesPackageBase;

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
