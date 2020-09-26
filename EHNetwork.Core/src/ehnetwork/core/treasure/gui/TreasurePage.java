package ehnetwork.core.treasure.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.core.treasure.TreasureLocation;
import ehnetwork.core.treasure.TreasureManager;
import ehnetwork.core.treasure.TreasureType;

public class TreasurePage extends ShopPageBase<TreasureManager, TreasureShop>
{
	private TreasureLocation _treasureLocation;
	private InventoryManager _inventoryManager;

	public TreasurePage(TreasureManager plugin, TreasureShop shop, TreasureLocation treasureLocation, CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Open Treasure", player, 9);

		_treasureLocation = treasureLocation;
		_inventoryManager = inventoryManager;

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		int basicCount = _inventoryManager.Get(getPlayer()).getItemCount(TreasureType.OLD.getItemName());
		int heroicCount = _inventoryManager.Get(getPlayer()).getItemCount(TreasureType.ANCIENT.getItemName());
		int legendaryCount = _inventoryManager.Get(getPlayer()).getItemCount(TreasureType.MYTHICAL.getItemName());

		List<String> basicLore = new ArrayList<String>();
		basicLore.add(" ");
		basicLore.add(F.value("Old Chests Owned", "" + basicCount));
		basicLore.add(" ");
		basicLore.add(C.cGray + "We've scoured the lands of Minecraft");
		basicLore.add(C.cGray + "and found these abandoned chests.");
		basicLore.add(C.cGray + "The contents are unknown, but");
		basicLore.add(C.cGray + "according to the inscriptions on the");
		basicLore.add(C.cGray + "the straps they appear to contain");
		basicLore.add(C.cGray + "many kinds of loot.");
		basicLore.add(" ");
		if (basicCount > 0)
			basicLore.add(ChatColor.RESET + C.cGreen + "Click to Open!");
		else
		{
			basicLore.add(ChatColor.RESET + "Click to Purchase for " + C.cYellow + "1000 Coins");
			basicLore.add(" ");
			basicLore.add(ChatColor.RESET + "or Request at: " + C.cYellow + "discord.gg/FttmSEQ");
		}

		List<String> heroicLore = new ArrayList<String>();
		heroicLore.add(" ");
		heroicLore.add(F.value("Ancient Chests Owned", "" + heroicCount));
		heroicLore.add(" ");
		heroicLore.add(C.cGray + "Some of our bravest adventurers");
		heroicLore.add(C.cGray + "have discovered these chests within ");
		heroicLore.add(C.cGray + "temples hidden in Minecrafts worlds.");
		heroicLore.add(" ");
		if (heroicCount > 0)
			heroicLore.add(ChatColor.RESET + C.cGreen + "Click to Open!");
		else
		{
			heroicLore.add(ChatColor.RESET + "Click to Purchase for " + C.cYellow + "5000 Coins");
			heroicLore.add(" ");
			heroicLore.add(ChatColor.RESET + "or Request at: " + C.cYellow + "discord.gg/FttmSEQ");
		}

		List<String> legendaryLore = new ArrayList<String>();
		legendaryLore.add(" ");
		legendaryLore.add(F.value("Mythical Chests Owned", "" + legendaryCount));
		legendaryLore.add(" ");
		legendaryLore.add(C.cGray + "All our previous adventurers have");
		legendaryLore.add(C.cGray + "perished in search of these chests.");
		legendaryLore.add(C.cGray + "However, legends of their existence");
		legendaryLore.add(C.cGray + "convinced Sterling, Chiss and Defek7");
		legendaryLore.add(C.cGray + "to venture out and discover the");
		legendaryLore.add(C.cGray + "location of these chests on their own.");
		legendaryLore.add(" ");
		if (legendaryCount > 0)
			legendaryLore.add(ChatColor.RESET + C.cGreen + "Click to Open!");
		else
		{
			legendaryLore.add(ChatColor.RESET + "Click to Purchase for " + C.cYellow + "10000 Coins");
			legendaryLore.add(" ");
			legendaryLore.add(ChatColor.RESET + "or Request at: " + C.cYellow + "discord.gg/FttmSEQ");
		}

		ShopItem basic = new ShopItem(Material.CHEST, C.cGreen + C.Bold + "Old Chest", basicLore.toArray(new String[0]), 0, false, false);
		ShopItem heroic = new ShopItem(Material.TRAPPED_CHEST, C.cGold + C.Bold + "Ancient Chest", heroicLore.toArray(new String[0]), 0, false, false);
		ShopItem legendary = new ShopItem(Material.ENDER_CHEST, C.cRed + C.Bold + "Mythical Chest", legendaryLore.toArray(new String[0]), 0, false, false);

		if (basicCount > 0)		addButton(2, basic, new OpenTreasureButton(getPlayer(), _treasureLocation, TreasureType.OLD));
		else					addButton(2, basic, new BuyChestButton(getPlayer(), _inventoryManager, this, "Old Chest", Material.CHEST, 1000));
		
		if (heroicCount > 0)	addButton(4, heroic, new OpenTreasureButton(getPlayer(), _treasureLocation, TreasureType.ANCIENT));
		else					addButton(4, heroic, new BuyChestButton(getPlayer(), _inventoryManager, this, "Ancient Chest", Material.CHEST, 5000));
		
		if (legendaryCount > 0)	addButton(6, legendary, new OpenTreasureButton(getPlayer(), _treasureLocation, TreasureType.MYTHICAL));
		else					addButton(6, legendary, new BuyChestButton(getPlayer(), _inventoryManager, this, "Mythical Chest", Material.ENDER_CHEST, 10000));
	}
}
