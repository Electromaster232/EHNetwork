package ehnetwork.game.skyclans.shop.pvp;

import org.bukkit.Material;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.game.skyclans.clans.ClansManager;
import ehnetwork.game.skyclans.shop.PvpItem;
import ehnetwork.game.skyclans.shop.PvpShopButton;

public class PvpPage extends ShopPageBase<ClansManager, PvpShop>
{
	public PvpPage(ClansManager plugin, PvpShop shop, CoreClientManager clientManager, DonationManager donationManager, org.bukkit.entity.Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Pvp Gear", player);
		
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		addPvpItem(9, new PvpItem(Material.GOLD_HELMET, (byte)0, 1, "Mage Helmet", 200, 4));
		addPvpItem(18, new PvpItem(Material.GOLD_CHESTPLATE, (byte)0, 1, "Mage Chestplate", 200, 4));
		addPvpItem(27, new PvpItem(Material.GOLD_LEGGINGS, (byte)0, 1, "Mage Leggings", 200, 4));
		addPvpItem(36, new PvpItem(Material.GOLD_BOOTS, (byte)0, 1, "Mage Boots", 200, 4));
		
		addPvpItem(10, new PvpItem(Material.LEATHER_HELMET, (byte)0, 1, "Assassin Helmet", 200, 4));
		addPvpItem(19, new PvpItem(Material.LEATHER_CHESTPLATE, (byte)0, 1, "Assassin Chestplate", 200, 4));
		addPvpItem(28, new PvpItem(Material.LEATHER_LEGGINGS, (byte)0, 1, "Assassin Leggings", 200, 4));
		addPvpItem(37, new PvpItem(Material.LEATHER_BOOTS, (byte)0, 1, "Assassin Boots", 200, 4));
		
		addPvpItem(11, new PvpItem(Material.CHAINMAIL_HELMET, (byte)0, 1, "Ranger Helmet", 200, 4));
		addPvpItem(20, new PvpItem(Material.CHAINMAIL_CHESTPLATE, (byte)0, 1, "Ranger Chestplate", 200, 4));
		addPvpItem(29, new PvpItem(Material.CHAINMAIL_LEGGINGS, (byte)0, 1, "Ranger Leggings", 200, 4));
		addPvpItem(38, new PvpItem(Material.CHAINMAIL_BOOTS, (byte)0, 1, "Ranger Boots", 200, 4));

		addPvpItem(12, new PvpItem(Material.IRON_HELMET, (byte)0, 1, "Knight Helmet", 200, 4));
		addPvpItem(21, new PvpItem(Material.IRON_CHESTPLATE, (byte)0, 1, "Knight Chestplate", 200, 4));
		addPvpItem(30, new PvpItem(Material.IRON_LEGGINGS, (byte)0, 1, "Knight Leggings", 200, 4));
		addPvpItem(39, new PvpItem(Material.IRON_BOOTS, (byte)0, 1, "Knight Boots", 200, 4));

		addPvpItem(13, new PvpItem(Material.DIAMOND_HELMET, (byte)0, 1, "Brute Helmet", 200, 4));
		addPvpItem(22, new PvpItem(Material.DIAMOND_CHESTPLATE, (byte)0, 1, "Brute Chestplate", 200, 4));
		addPvpItem(31, new PvpItem(Material.DIAMOND_LEGGINGS, (byte)0, 1, "Brute Leggings", 200, 4));
		addPvpItem(40, new PvpItem(Material.DIAMOND_BOOTS, (byte)0, 1, "Brute Boots", 200, 4));
		
		addPvpItem(15, new PvpItem(Material.IRON_SWORD, (byte)0, 1, "Iron Sword", 100, 4));
		addPvpItem(16, new PvpItem(Material.DIAMOND_SWORD, (byte)0, 1, "Power Sword", 800, 4));
		addPvpItem(17, new PvpItem(Material.GOLD_SWORD, (byte)0, 1, "Booster Sword", 800, 4));
		
		addPvpItem(24, new PvpItem(Material.IRON_AXE, (byte)0, 1, "Iron Axe", 100, 4));
		addPvpItem(25, new PvpItem(Material.DIAMOND_AXE, (byte)0, 1, "Power Axe", 800, 4));
		addPvpItem(26, new PvpItem(Material.GOLD_AXE, (byte)0, 1, "Booster Axe", 800, 4));
		
		addPvpItem(33, new PvpItem(Material.BOW, (byte)0, 1, "Standard Bow", 100, 4));
		addPvpItem(34, new PvpItem(Material.ARROW, (byte)0, 16, "Arrows", 50, 4));

		addPvpItem(51, new PvpItem(Material.TNT, (byte)0, 1, "TNT", 500, 16));
		addPvpItem(52, new PvpItem(Material.BREWING_STAND_ITEM, (byte)0, 1, "TNT Generator", 5000));
		addPvpItem(53, new PvpItem(Material.ENCHANTMENT_TABLE, (byte)0, 1, "Class Shop", 10000));
	}
	
	public void addPvpItem(int slot, PvpItem item)
	{
		addButton(slot, item, new PvpShopButton<PvpPage>(this, item));
	}
}
