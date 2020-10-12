package ehnetwork.core.treasure;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.shop.item.SalesPackageBase;

/**
 * Created by shaun on 14-09-18.
 */
public class TreasureKey extends SalesPackageBase
{
	public TreasureKey()
	{
		super("Treasure Key", Material.TRIPWIRE_HOOK, (byte) 0, new String[] { ChatColor.RESET + "Used to open Treasure Chests" }, 1000);

		KnownPackage = false;
		OneTimePurchaseOnly = false;
	}

	@Override
	public void Sold(Player player, CurrencyType currencyType)
	{

	}
}
