package ehnetwork.game.skyclans.shop;

import org.bukkit.Material;

import ehnetwork.core.common.util.C;
import ehnetwork.core.shop.item.ShopItem;

public class PvpItem extends ShopItem
{
	private static String LEFT_CLICK_BUY = C.cYellow + "Left-Click" + C.cWhite + " to Buy " + C.cGreen + 1;
	private static String RIGHT_CLICK_SELL = C.cYellow + "Right-Click" + C.cWhite + " to Sell " + C.cGreen + 1;
	
	private int _price;
	private int _bulkCount;
	
	public PvpItem(Material type, byte data, int displayAmount, String name, int price, int bulkCount)
	{
		super(type, data, name, new String[] 
				{
					C.cWhite + " ",
					LEFT_CLICK_BUY,
					C.cWhite + "Costs " + C.cGreen + "$" + price,
					C.cWhite + " ",
					C.cYellow + "Shift Left-Click" + C.cWhite + " to Buy " + C.cGreen + bulkCount,
					C.cWhite + "Costs " + C.cGreen + "$" + (price * bulkCount),
					C.cWhite + " ",
					RIGHT_CLICK_SELL,
					C.cWhite + "Earns " + C.cGreen + "$" + (int)(price / 2),
					C.cWhite + " ",
					C.cYellow + "Shift Right-Click" + C.cWhite + " to Sell " + C.cGreen + "All",
				}, 0, false, false);
		
		_price = price;
		_bulkCount = bulkCount;
	}
	
	public PvpItem(Material type, byte data, int displayAmount, String name, int price)
	{
		super(type, data, name, new String[] 
				{
					C.cWhite + " ",
					LEFT_CLICK_BUY,
					C.cWhite + "Costs " + C.cGreen + "$" + price,
					C.cWhite + " ",
					RIGHT_CLICK_SELL,
					C.cWhite + "Earns " + C.cGreen + "$" + (int)(price / 2),
					C.cWhite + " ",
					C.cYellow + "Shift Right-Click" + C.cWhite + " to Sell " + C.cGreen + "All",
				}, 0, false, false);
		
		_price = price;
		_bulkCount = -1;
	}
	
	public int getPrice()
	{
		return _price;
	}
	
	public int getBulkCount()
	{
		return _bulkCount;
	}
}
