package ehnetwork.core.treasure.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.page.ConfirmationPage;
import ehnetwork.core.treasure.ChestPackage;
import ehnetwork.core.treasure.TreasureManager;

public class BuyChestButton implements IButton
{
	private InventoryManager _inventoryManager;

	private TreasurePage _page;
	
	private String _chestName;
	private Material _chestMat;
	private int _chestCost;
	
	public BuyChestButton(Player player, InventoryManager inventoryManager, TreasurePage page,
			String chestName, Material chestMat, int chestCost)
	{		
		_inventoryManager = inventoryManager;

		_page = page;
		
		_chestName = chestName;
		_chestMat = chestMat;
		_chestCost = chestCost;
	}

	@Override
	public void onClick(final Player player, ClickType clickType)
	{
		_page.getShop().openPageForPlayer(player, new ConfirmationPage<TreasureManager, TreasureShop>(
				_page.getPlugin(), _page.getShop(), _page.getClientManager(), _page.getDonationManager(), new Runnable()
		{
			public void run()
			{
				_inventoryManager.addItemToInventory(player, "Item", _chestName, 1);
				_page.refresh();
			}
		}, _page, new ChestPackage(_chestName, _chestMat, _chestCost), CurrencyType.Coins, player));
	}
}