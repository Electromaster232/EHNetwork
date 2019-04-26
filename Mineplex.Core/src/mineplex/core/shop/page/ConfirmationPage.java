package mineplex.core.shop.page;

import mineplex.core.MiniPlugin;
import mineplex.core.donation.DonationManager;
import mineplex.core.server.util.TransactionResponse;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.SalesPackageBase;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ConfirmationPage<PluginType extends MiniPlugin, ShopType extends ShopBase<PluginType>> extends ShopPageBase<PluginType, ShopType> implements Runnable
{
	private Runnable _runnable;
	private ShopPageBase<PluginType, ShopType> _returnPage;
	private SalesPackageBase _salesItem;
	private int _okSquareSlotStart;
	private boolean _processing;
	private int _progressCount;
	private ShopItem _progressItem;
	private int _taskId;
	
	public ConfirmationPage(PluginType plugin, ShopType shop, CoreClientManager clientManager, DonationManager donationManager, Runnable runnable, ShopPageBase<PluginType, ShopType> returnPage, SalesPackageBase salesItem, CurrencyType currencyType, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "            Confirmation", player);
		
		_runnable = runnable;
		_returnPage = returnPage;
		_salesItem = salesItem;
		setCurrencyType(currencyType);
		_progressItem = new ShopItem(Material.LAPIS_BLOCK, (byte)11, ChatColor.BLUE + "Processing", null, 1, false, true);
		_okSquareSlotStart = 27;
		
		if (getShop().canPlayerAttemptPurchase(player))
		{
			buildPage();
		}
		else
		{
			buildErrorPage(new String[]{ChatColor.RED + "You have attempted too many invalid transactions.", ChatColor.RED + "Please wait 10 seconds before retrying."});
			_taskId = plugin.getScheduler().scheduleSyncRepeatingTask(plugin.getPlugin(), this, 2L, 2L);
		}
	}
	
	protected void buildPage()
	{
		this.getInventory().setItem(22, new ShopItem(_salesItem.GetDisplayMaterial(), (byte)0, _salesItem.GetDisplayName(), _salesItem.GetDescription(), 1, false, true).getHandle());

		IButton okClicked = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				okClicked(player);
			}
		};
		
		IButton cancelClicked = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				cancelClicked(player);
			}
		};
		
		buildSquareAt(_okSquareSlotStart, new ShopItem(Material.EMERALD_BLOCK, (byte) 0, ChatColor.GREEN + "OK", null, 1, false, true), okClicked);
		buildSquareAt(_okSquareSlotStart + 6, new ShopItem(Material.REDSTONE_BLOCK, (byte) 0, ChatColor.RED + "CANCEL", null, 1, false, true), cancelClicked);
		
		this.getInventory().setItem(4, new ShopItem(getCurrencyType().GetDisplayMaterial(), (byte)0, getCurrencyType().toString(), new String[] { C.cGray + _salesItem.GetCost(getCurrencyType()) + " " + getCurrencyType().toString() + " will be deducted from your account balance." }, 1, false, true).getHandle());
	}
	
	protected void okClicked(Player player)
	{
		processTransaction();
	}
	
	protected void cancelClicked(Player player)
	{
		getPlugin().getScheduler().cancelTask(_taskId);
		
		if (_returnPage != null)
			getShop().openPageForPlayer(player, _returnPage);
		else
		{
			player.closeInventory();
		}
			
	}
	
	private void buildSquareAt(int slot, ShopItem item, IButton button)
	{
		addButton(slot, item, button);
		addButton(slot + 1, item, button);
		addButton(slot + 2, item, button);
		
		slot += 9;
		
		addButton(slot, item, button);
		addButton(slot + 1, item, button);
		addButton(slot + 2, item, button);
		
		slot += 9;
		
		addButton(slot, item, button);
		addButton(slot + 1, item, button);
		addButton(slot + 2, item, button);
	}
	
	private void processTransaction()
	{
		for (int i=_okSquareSlotStart; i < 54; i++)
		{
			getButtonMap().remove(i);
			clear(i);
		}
		
		_processing = true;
		
		if (_salesItem.IsKnown())
		{
			getDonationManager().PurchaseKnownSalesPackage(new Callback<TransactionResponse>()
			{
				public void run(TransactionResponse response)
				{
					showResultsPage(response);
				}
			}, getPlayer().getName(), getPlayer().getUniqueId(), _salesItem.GetCost(getCurrencyType()), _salesItem.GetSalesPackageId());
		}
		else
		{
			getDonationManager().PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
			{
				public void run(TransactionResponse response)
				{
					showResultsPage(response);
				}
			}, getPlayer().getName(), getClientManager().Get(getPlayer()).getAccountId(), _salesItem.GetName(), getCurrencyType() == CurrencyType.Coins, _salesItem.GetCost(getCurrencyType()), _salesItem.OneTimePurchase());
		}
		
		_taskId = getPlugin().getScheduler().scheduleSyncRepeatingTask(getPlugin().getPlugin(), this, 2L, 2L);
	}

	private void showResultsPage(TransactionResponse response)
	{
		_processing = false;
		
		switch (response)
		{
			case Failed:
				buildErrorPage(ChatColor.RED + "There was an error processing your request.");
				getShop().addPlayerProcessError(getPlayer());
				break;
			case AlreadyOwns:
				buildErrorPage(ChatColor.RED + "You already own this package.");
				getShop().addPlayerProcessError(getPlayer());
				break;
			case InsufficientFunds:
				buildErrorPage(ChatColor.RED + "Your account has insufficient funds.");
				getShop().addPlayerProcessError(getPlayer());
				break;
			case Success:
				_salesItem.Sold(getPlayer(), getCurrencyType());
				
				buildSuccessPage("Your purchase was successful.");
				
				if (_runnable != null)
					_runnable.run();
				
				break;
			default:
				break;
		}
		
		_progressCount = 0;
	}

	private void buildErrorPage(String... message)
	{
		IButton returnButton = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				cancelClicked(player);
			}
		};
		
		ShopItem item = new ShopItem(Material.REDSTONE_BLOCK, (byte)0, ChatColor.RED + "" + ChatColor.UNDERLINE + "ERROR", message, 1, false, true);
		for (int i = 0; i < this.getSize(); i++)
		{
			addButton(i, item, returnButton);
		}
		
		getPlayer().playSound(getPlayer().getLocation(), Sound.BLAZE_DEATH, 1, .1f);
	}
	
	private void buildSuccessPage(String message)
	{
		IButton returnButton = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				cancelClicked(player);
			}
		};
		
		ShopItem item = new ShopItem(Material.EMERALD_BLOCK, (byte)0, ChatColor.GREEN + message, null, 1, false, true);
		for (int i = 0; i < this.getSize(); i++)
		{
			addButton(i, item, returnButton);
		}
		
		getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_PLING, 1, .9f);
	}
	
	@Override
	public void playerClosed()
	{
		super.playerClosed();
		
		Bukkit.getScheduler().cancelTask(_taskId);
		
		if (_returnPage != null && getShop() != null)
			getShop().setCurrentPageForPlayer(getPlayer(), _returnPage);
	}
	
	@Override
	public void run() 
	{
		if (_processing)
		{
			if (_progressCount == 9)
			{
				for (int i=45; i < 54; i++)
				{
					clear(i);
				}
				
				_progressCount = 0;
			}
			
			setItem(45 + _progressCount, _progressItem);
		}
		else
		{
			if (_progressCount >= 20)
			{
				try
				{
					Bukkit.getScheduler().cancelTask(_taskId);
					
					if (_returnPage != null && getShop() != null)
					{
						getShop().openPageForPlayer(getPlayer(), _returnPage);
					}
					else if (getPlayer() != null)
					{
						getPlayer().closeInventory();
					}
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
				finally
				{
					dispose();
				}
			}
		}
		
		_progressCount++;
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		Bukkit.getScheduler().cancelTask(_taskId);
	}
}
