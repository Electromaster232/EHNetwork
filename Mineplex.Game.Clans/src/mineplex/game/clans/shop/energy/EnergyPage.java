package mineplex.game.clans.shop.energy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.donation.DonationManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.game.clans.clans.ClanInfo;
import mineplex.game.clans.clans.ClansManager;

public class EnergyPage extends ShopPageBase<ClansManager, EnergyShop>
{
	private ClanInfo _clanInfo;

	public EnergyPage(ClansManager plugin, EnergyShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Energy Shop", player, 9);
		_clanInfo = getPlugin().getClan(getPlayer());

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		if (_clanInfo == null)
		{
			buildNoClan();
			return;
		}
		else if (_clanInfo.getEnergyPurchasable() > 0)
		{
			if (hasEnoughGold())
			{
				buildPurchasable(_clanInfo);
			}
			else
			{
				buildNotEnoughGold(_clanInfo);
			}
		}
		else
		{
			buildMaxEnergy(_clanInfo);
		}
	}

	private void buildPurchasable(ClanInfo _clanInfo)
	{
		int playerGold = getPlayerGold();
		int energyCost = getEnergyPerHour();
		int goldCost = getGoldPerHour();

		EnergyShopButton button = new EnergyShopButton(getPlayer(), getPlugin(), this, _clanInfo, true, getDonationManager());
		String title = "Purchase Energy";
		String currentEnergy = ChatColor.RESET + F.value("Clan Energy", "" + _clanInfo.getEnergy());
		String energyString = ChatColor.RESET + F.value("Energy drain/hour", "" + energyCost);
		String playerGoldString = ChatColor.RESET + F.value("Your Gold", playerGold + "g");
		String purchaseString = ChatColor.RESET + C.cWhite + "Click to buy 1 hour of energy for " + C.cYellow + goldCost + "g";
		ShopItem shopItem = new ShopItem(Material.LAVA_BUCKET, title, new String[] {" ", currentEnergy, energyString, " ", playerGoldString, purchaseString}, 0, false, false);
		addButton(4, shopItem, button);
	}

	private void buildNotEnoughGold(ClanInfo _clanInfo)
	{
		int playerGold = getPlayerGold();
		int energyCost = getEnergyPerHour();
		int goldCost = getGoldPerHour();

		EnergyShopButton button = new EnergyShopButton(getPlayer(), getPlugin(), this, _clanInfo, true, getDonationManager());
		String title = ChatColor.RED + C.Bold + "Missing Gold!";
		String currentEnergy = ChatColor.RESET + F.value("Clan Energy", "" + _clanInfo.getEnergy());
		String energyString = ChatColor.RESET + F.value("Energy drain/hour", "" + energyCost);
		String playerGoldString = ChatColor.RESET + F.value("Your Gold", playerGold + "g");
		String purchaseString = ChatColor.RESET + C.cWhite + "You don't have enough gold";
		String goldString = ChatColor.RESET + C.cWhite + "You need " + C.cYellow + goldCost + C.cWhite + " gold to purchase energy";
		ShopItem shopItem = new ShopItem(Material.GOLD_INGOT, title, new String[] {" ", currentEnergy, energyString, " ", playerGoldString, purchaseString, goldString}, 0, false, false);
		addButton(4, shopItem, button);
	}

	private void buildNoClan()
	{
		ShopItem shopItem = new ShopItem(Material.GLASS, "No Clan!", new String[] {" ", "You need to be in a", "clan to purchase energy!"}, 1, false, false);
		setItem(4, shopItem);
	}

	private void buildMaxEnergy(ClanInfo _clanInfo)
	{
		int energy = _clanInfo.getEnergy();
		String title = C.cRed + C.Bold + "Cannot Purchase Energy!";
		String currentEnergy = ChatColor.RESET + F.value("Clan Energy", "" + energy);
		String[] lore = new String[] {" ", currentEnergy,  ChatColor.RESET + "Your clan is at the energy cap"};
		ShopItem shopItem = new ShopItem(Material.BUCKET, title, lore, 0, false, false);
		addItem(4, shopItem);
	}

	protected boolean hasEnoughGold()
	{
		return getPlayerGold() >= getGoldPerHour();
	}

	protected int getGoldPerHour()
	{
		return getPlugin().convertEnergyToGold(getEnergyPerHour());
	}

	protected int getEnergyPerHour()
	{
		return _clanInfo.getEnergyCostPerMinute() * 60;
	}

	protected int getPlayerGold()
	{
		return getDonationManager().Get(getPlayer()).getGold();
	}

	public void showLoading()
	{
		ItemStack is = ItemStackFactory.Instance.CreateStack(Material.WOOL, (byte) 11, 1, C.cAqua + C.Bold + "Please Wait..");
		setItem(4, is);
	}
}
