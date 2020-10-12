package ehnetwork.core.shop.page;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClient;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.shop.ShopBase;
import ehnetwork.core.shop.item.IButton;

public abstract class ShopPageBase<PluginType extends MiniPlugin, ShopType extends ShopBase<PluginType>> extends CraftInventoryCustom implements Listener
{
	private PluginType _plugin;
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private ShopType _shop;
	private Player _player;
	private CoreClient _client;
	private CurrencyType _currencyType;
	private NautHashMap<Integer, IButton> _buttonMap;
	private boolean _showCurrency = false;
	
	private int _currencySlot = 4;
	
	public ShopPageBase(PluginType plugin, ShopType shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player) 
	{
		this(plugin, shop, clientManager, donationManager, name, player, 54);
	}
	
	public ShopPageBase(PluginType plugin, ShopType shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player, int slots) 
	{
		super(null, slots, name);
		
		_plugin = plugin;
		_clientManager = clientManager;
		_donationManager = donationManager;
		_shop = shop;
		_player = player;
		_buttonMap = new NautHashMap<Integer, IButton>();
		
		_client = _clientManager.Get(player);
				
		if (shop.getAvailableCurrencyTypes().size() > 0)
		{
			_currencyType = shop.getAvailableCurrencyTypes().get(0);
		}
	}

	protected void changeCurrency(Player player)
	{
		playAcceptSound(player);
    	
		int currentIndex = _shop.getAvailableCurrencyTypes().indexOf(_currencyType);
		
		if (currentIndex + 1 < _shop.getAvailableCurrencyTypes().size())
		{
			_currencyType = _shop.getAvailableCurrencyTypes().get(currentIndex + 1);
		}
		else
		{
			_currencyType = _shop.getAvailableCurrencyTypes().get(0);
		}
	}

	protected abstract void buildPage();

	protected void addItem(int slot, ItemStack item)
	{
		if (slot > inventory.getSize() - 1)
		{
			_player.getInventory().setItem(getPlayerSlot(slot), item);
		}
		else
		{
			setItem(slot, item);
		}
	}

	protected void addItemFakeCount(int slot, ItemStack item, int fakeCount)
	{
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		nmsStack.count = fakeCount;

		if (slot > inventory.getSize() - 1)
		{
			((CraftPlayer) _player).getHandle().inventory.setItem(getPlayerSlot(slot), nmsStack);
		}
		else
		{
			getInventory().setItem(slot, nmsStack);
		}
	}
	
	protected int getPlayerSlot(int slot)
	{
		return slot >= (inventory.getSize() + 27) ? slot - (inventory.getSize() + 27) : slot - (inventory.getSize() - 9);
	}
	
	protected void addButton(int slot, ItemStack item, IButton button)
	{
		addItem(slot, item);
		
		_buttonMap.put(slot, button);
	}

	protected void addButtonFakeCount(int slot, ItemStack item, IButton button, int fakeItemCount)
	{
		addItemFakeCount(slot, item, fakeItemCount);

		_buttonMap.put(slot, button);
	}

	protected void addGlow(int slot)
	{
		UtilInv.addDullEnchantment(getItem(slot));
	}
	
	protected void removeButton(int slot)
	{
		getInventory().setItem(slot, null);
		_buttonMap.remove(slot);
	}

	public void playerClicked(InventoryClickEvent event)
	{
		if (_buttonMap.containsKey(event.getRawSlot()))
		{
			_buttonMap.get(event.getRawSlot()).onClick(_player, event.getClick());
		}
		else if (event.getRawSlot() != -999)
		{
			if (event.getInventory().getTitle() == inventory.getName() && (inventory.getSize() <= event.getSlot() || inventory.getItem(event.getSlot()) != null))
			{
				playDenySound(_player);
			}
			else if (event.getInventory() == _player.getInventory() && _player.getInventory().getItem(event.getSlot()) != null)
			{
				playDenySound(_player);
			}
		}
	}
	
	public void playerOpened()
	{
		
	}

	public void playerClosed()
	{
		this.inventory.onClose((CraftPlayer) _player);
	}
	
	public void playAcceptSound(Player player)
	{
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.6f);
	}
	
	public void playRemoveSound(Player player)
	{
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.6f);
	}

	public void playDenySound(Player player)
	{
		player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
	}
	
	public void dispose()
	{
		_player = null;
		_client = null;
		_shop = null;
		_plugin = null;
	}

	public void refresh()
	{
		clearPage();
		buildPage();
	}

	public void clearPage()
	{
		clear();
		_buttonMap.clear();
	}

	public void setItem(int column, int row, ItemStack itemStack)
	{
		setItem(column + (row * 9), itemStack);
	}
	
	public ShopType getShop()
	{
		return _shop;
	}
	
	public PluginType getPlugin()
	{
		return _plugin;
	}
	
	public CoreClientManager getClientManager()
	{
		return _clientManager;
	}
	
	public DonationManager getDonationManager()
	{
		return _donationManager;
	}

	protected Player getPlayer()
	{
		return _player;
	}

	protected CoreClient getClient()
	{
		return _client;
	}

	protected CurrencyType getCurrencyType()
	{
		return _currencyType;
	}

	protected void setCurrencyType(CurrencyType type)
	{
		_currencyType = type;
	}

	protected NautHashMap<Integer, IButton> getButtonMap()
	{
		return _buttonMap;
	}

	protected boolean shouldShowCurrency()
	{
		return _showCurrency;
	}

	protected int getCurrencySlot()
	{
		return _currencySlot;
	}
}
