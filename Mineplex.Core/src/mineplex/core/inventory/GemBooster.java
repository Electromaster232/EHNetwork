package mineplex.core.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mineplex.core.common.CurrencyType;
import mineplex.core.common.util.C;
import mineplex.core.gadget.types.ItemGadget;
import mineplex.core.shop.item.SalesPackageBase;

public class GemBooster extends SalesPackageBase
{
	public GemBooster(boolean enabled, int gemBoosters)
	{
		super("20 Gem Booster Pack", Material.EMERALD, (byte)0, new String[] 
				{
					C.cYellow + "1000 Coins",
					" ",
					(enabled ? C.cGreen + "Left-Click To Use:" : ""),
					C.cWhite + "Use these before games start to",
					C.cWhite + "boost the amount of Gems earned",
					C.cWhite + "for all players in the game!",
					" ",
		    		C.cGreen + "Right-Click To Purchase:",
		    		C.cWhite + "20 Gem Boosters for " + C.cYellow + "1000 Coins",
		    		" ",
					C.cWhite + "Your Gem Boosters: " + C.cGreen + gemBoosters
				}, 1000, 20);
		
		KnownPackage = false;
		OneTimePurchaseOnly = false;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{
	}
}
