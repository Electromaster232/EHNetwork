package ehnetwork.core.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.npc.event.NpcDamageByEntityEvent;
import ehnetwork.core.npc.event.NpcInteractEntityEvent;
import ehnetwork.core.shop.page.ShopPageBase;

public abstract class ShopBase<PluginType extends MiniPlugin> implements Listener
{
	private NautHashMap<String, Long> _errorThrottling;
	private NautHashMap<String, Long> _purchaseBlock;
	
	private List<CurrencyType> _availableCurrencyTypes;
	
	private PluginType _plugin;
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private String _name;
	private NautHashMap<String, ShopPageBase<PluginType, ? extends ShopBase<PluginType>>> _playerPageMap;
	
	private HashSet<String> _openedShop = new HashSet<String>();
	
	public ShopBase(PluginType plugin, CoreClientManager clientManager, DonationManager donationManager, String name, CurrencyType...currencyTypes) 
	{		
		_plugin = plugin;
		_clientManager = clientManager;
		_donationManager = donationManager;
		_name = name;
		
		_playerPageMap = new NautHashMap<String, ShopPageBase<PluginType, ? extends ShopBase<PluginType>>>();
		_errorThrottling = new NautHashMap<String, Long>();
		_purchaseBlock = new NautHashMap<String, Long>();
		
		_availableCurrencyTypes = new ArrayList<CurrencyType>();
		_availableCurrencyTypes.addAll(Arrays.asList(currencyTypes));
		
		_plugin.registerEvents(this);
	}
	
	public List<CurrencyType> getAvailableCurrencyTypes()
	{
		return _availableCurrencyTypes;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamageEntity(NpcDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
    		if (attemptShopOpen((Player) event.getDamager(), event.getNpc()))
    		{
    			event.setCancelled(true);
    		}	    			
		}
	}
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(NpcInteractEntityEvent event)
    {
		if (attemptShopOpen(event.getPlayer(), (LivingEntity) event.getNpc()))
			event.setCancelled(true);
    }
    
    private boolean attemptShopOpen(Player player, LivingEntity entity)
    {
		if (!_openedShop.contains(player.getName()) && entity.isCustomNameVisible() && entity.getCustomName() != null && ChatColor.stripColor(entity.getCustomName()).equalsIgnoreCase(ChatColor.stripColor(_name)))
		{
			if (!canOpenShop(player))
				return false;
			
			_openedShop.add(player.getName());
			
    		openShopForPlayer(player);
    		if (!_playerPageMap.containsKey(player.getName()))
    		{
    			_playerPageMap.put(player.getName(), buildPagesFor(player));
    		}
    		
    		openPageForPlayer(player, getOpeningPageForPlayer(player));
    		
    		return true;
		}
		
		return false;
    }
    
    public boolean attemptShopOpen(Player player)
    {
		if (!_openedShop.contains(player.getName()))
		{
			if (!canOpenShop(player))
				return false;
			
			_openedShop.add(player.getName());
			
    		openShopForPlayer(player);
    		if (!_playerPageMap.containsKey(player.getName()))
    		{
    			_playerPageMap.put(player.getName(), buildPagesFor(player));
    		}
    		
    		openPageForPlayer(player, getOpeningPageForPlayer(player));
    		
    		return true;
		}
		
		return false;
    }
    
	protected ShopPageBase<PluginType, ? extends ShopBase<PluginType>> getOpeningPageForPlayer(Player player)
	{
		return _playerPageMap.get(player.getName());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (_playerPageMap.containsKey(event.getWhoClicked().getName()) && _playerPageMap.get(event.getWhoClicked().getName()).getName().equalsIgnoreCase(event.getInventory().getName()))
		{
			_playerPageMap.get(event.getWhoClicked().getName()).playerClicked(event);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if (_playerPageMap.containsKey(event.getPlayer().getName()) && _playerPageMap.get(event.getPlayer().getName()).getTitle() != null && _playerPageMap.get(event.getPlayer().getName()).getTitle().equalsIgnoreCase(event.getInventory().getTitle()))
		{
			_playerPageMap.get(event.getPlayer().getName()).playerClosed();
			_playerPageMap.get(event.getPlayer().getName()).dispose();
			
			_playerPageMap.remove(event.getPlayer().getName());
			
			closeShopForPlayer((Player) event.getPlayer());
			
			_openedShop.remove(event.getPlayer().getName());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		if (!event.isCancelled())
			return;

		if (_playerPageMap.containsKey(event.getPlayer().getName()) && _playerPageMap.get(event.getPlayer().getName()).getTitle() != null && _playerPageMap.get(event.getPlayer().getName()).getTitle().equalsIgnoreCase(event.getInventory().getTitle()))
		{
			_playerPageMap.get(event.getPlayer().getName()).playerClosed();
			_playerPageMap.get(event.getPlayer().getName()).dispose();

			_playerPageMap.remove(event.getPlayer().getName());

			closeShopForPlayer((Player) event.getPlayer());

			_openedShop.remove(event.getPlayer().getName());
		}
	}
	
	protected boolean canOpenShop(Player player)
	{
		return true;
	}
	
	protected void openShopForPlayer(Player player) { }
	
	protected void closeShopForPlayer(Player player) { }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (_playerPageMap.containsKey(event.getPlayer().getName()))
		{
			_playerPageMap.get(event.getPlayer().getName()).playerClosed();
			_playerPageMap.get(event.getPlayer().getName()).dispose();
			
			event.getPlayer().closeInventory();
			closeShopForPlayer(event.getPlayer());
			
			_playerPageMap.remove(event.getPlayer().getName());
			
			_openedShop.remove(event.getPlayer().getName());
		}
	}

	public void openPageForPlayer(Player player, ShopPageBase<PluginType, ? extends ShopBase<PluginType>> page)
	{
		if (_playerPageMap.containsKey(player.getName()))
		{
			_playerPageMap.get(player.getName()).playerClosed();
		}			
		
		setCurrentPageForPlayer(player, page);

	    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
	    if (nmsPlayer.activeContainer != nmsPlayer.defaultContainer)
	    {
	        // Do this so that other inventories know their time is over.
	        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer);
	        nmsPlayer.m();
	    }
		player.openInventory(page);
	}
	
	public void setCurrentPageForPlayer(Player player, ShopPageBase<PluginType, ? extends ShopBase<PluginType>> page)
	{
		_playerPageMap.put(player.getName(), page);
	}
	
	public void addPlayerProcessError(Player player)
	{
		if (_errorThrottling.containsKey(player.getName()) && (System.currentTimeMillis() - _errorThrottling.get(player.getName()) <= 5000))
			_purchaseBlock.put(player.getName(), System.currentTimeMillis());

		_errorThrottling.put(player.getName(), System.currentTimeMillis());
	}

	public boolean canPlayerAttemptPurchase(Player player)
	{
		return !_purchaseBlock.containsKey(player.getName()) || (System.currentTimeMillis() - _purchaseBlock.get(player.getName()) > 10000);
	}
	
	public NautHashMap<String, ShopPageBase<PluginType, ? extends ShopBase<PluginType>>> getPageMap()
	{
		return _playerPageMap;
	}
	
	protected abstract ShopPageBase<PluginType, ? extends ShopBase<PluginType>> buildPagesFor(Player player);

	public boolean isPlayerInShop(Player player)
	{
		return _playerPageMap.containsKey(player.getName());
	}

	protected PluginType getPlugin()
	{
		return _plugin;
	}

	protected CoreClientManager getClientManager()
	{
		return _clientManager;
	}

	protected DonationManager getDonationManager()
	{
		return _donationManager;
	}

	protected String getName()
	{
		return _name;
	}

	protected NautHashMap<String, ShopPageBase<PluginType, ? extends ShopBase<PluginType>>> getPlayerPageMap()
	{
		return _playerPageMap;
	}

	protected HashSet<String> getOpenedShop()
	{
		return _openedShop;
	}
}
