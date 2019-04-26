package mineplex.core.treasure.gui;

import org.bukkit.entity.Player;

import mineplex.core.account.CoreClientManager;
import mineplex.core.donation.DonationManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.treasure.TreasureLocation;
import mineplex.core.treasure.TreasureManager;

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
