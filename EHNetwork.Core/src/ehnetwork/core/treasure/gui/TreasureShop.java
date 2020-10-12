package ehnetwork.core.treasure.gui;

import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.core.treasure.TreasureLocation;
import ehnetwork.core.treasure.TreasureManager;

public class TreasureShop extends ShopBase<TreasureManager>
{
	private TreasureLocation _treasureLocation;
	private InventoryManager _inventoryManager;

	public TreasureShop(TreasureManager plugin, InventoryManager inventoryManager, CoreClientManager clientManager, DonationManager donationManager, TreasureLocation treasureLocation)
	{
		super(plugin, clientManager, donationManager, "Treasure Shop");
		_treasureLocation = treasureLocation;
		_inventoryManager = inventoryManager;
	}

	@Override
	protected ShopPageBase<TreasureManager, ? extends ShopBase<TreasureManager>> buildPagesFor(Player player)
	{
		return new TreasurePage(getPlugin(), this, _treasureLocation, getClientManager(), getDonationManager(), _inventoryManager, player);
	}
}
