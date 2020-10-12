package ehnetwork.hub.queue.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.party.Party;
import ehnetwork.core.shop.item.IButton;
import ehnetwork.core.shop.item.ShopItem;
import ehnetwork.core.shop.page.ShopPageBase;
import ehnetwork.hub.queue.PlayerMatchStatus;
import ehnetwork.hub.queue.QueueManager;

public class QueuePage extends ShopPageBase<QueueManager, QueueShop>
{
	private boolean _closeOnNextUpdate;
	private int _circleIndex = 0;
	
	public QueuePage(QueueManager plugin, QueueShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
	{
		super(plugin, shop, clientManager, donationManager, name, player, 54);
		
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		clear();
		
		if (getPlugin().isQueued(getPlayer()))
		{
			PlayerMatchStatus matchStatus = getPlugin().getQueuedPlayerStatus(getPlayer());
			
			if (matchStatus.AssignedMatch != -1)
			{
				if (matchStatus.State.equalsIgnoreCase("Ready"))
				{
					setItem(22, new ShopItem(Material.BOOK, "Waiting for players to accept.", new String[] { }, 1, false));
					
					int i = 45;
					
					for (String state : matchStatus.OtherStatuses)
					{
						if (state.equalsIgnoreCase("Awaiting Confirmation"))
						{
							setItem(i, new ShopItem(Material.WOOL, "Waiting for reply...", new String[] { }, 1, false));
							_closeOnNextUpdate = false;
						}
						else if (state.equalsIgnoreCase("Denied"))
						{
							setItem(i, new ShopItem(Material.WOOL, (byte)14, "Denied match.", new String[] { }, 1, false, false));
							_closeOnNextUpdate = false;
						}
						else if (state.equalsIgnoreCase("Ready"))
						{
							setItem(i, new ShopItem(Material.WOOL, (byte)5, "Accepted match.", new String[] { }, 1, false, false));
							_closeOnNextUpdate = true;
						}
						
						i++;
					}
				}
				else
				{
					IButton okClicked = new IButton()
					{
						@Override
						public void onClick(Player player, ClickType clickType)
						{
							OkClicked(player);
						}
					};
					
					IButton cancelClicked = new IButton()
					{
						@Override
						public void onClick(Player player, ClickType clickType)
						{
							CancelClicked(player);
						}
					};
					
					buildSquareAt(19, new ShopItem(Material.EMERALD_BLOCK, (byte)0, ChatColor.GREEN + "OK", null, 1, false, true), okClicked);
					buildSquareAt(23, new ShopItem(Material.REDSTONE_BLOCK, (byte)0, ChatColor.RED + "CANCEL", null, 1, false, true), cancelClicked);}
			}
			else
			{
				setItem(22, new ShopItem(Material.BOOK, "Looking for match...", new String[] { "Average wait time : DERP DERP DERP." }, 1, false));
				
				drawCircle();
			}
		}
		else
		{
			IButton queueButton = new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
					queuePlayer("Dominate", player);
				}
			};
			
			addButton(22, new ShopItem(Material.BOOK, "Play", new String[]{"Click me to enter play queue."}, 1, false), queueButton);
		}
	}

	public void Update()
	{
		if (_closeOnNextUpdate)
		{
			getPlayer().closeInventory();
			System.out.println(this.getClass().getName() + " 138");
			return;
		}
		
		_circleIndex++;
		_circleIndex %= 3;
		
		buildPage();
	}

	private void queuePlayer(String gameType, Player player)
	{
		Party party = getPlugin().getPartyManager().GetParty(player);
		
		if (party != null)
		{
			if (player.getName().equals(party.GetLeader()))
			{
				List<Player> players = new ArrayList<Player>();
				
				for (String name : party.GetPlayers())
				{
					Player partyPlayer = UtilPlayer.searchExact(name);
					
					if (partyPlayer == null)
						continue;

					players.add(partyPlayer);
				}
				
				getPlugin().queuePlayer(gameType, players.toArray(new Player[]{}));
			}
		}
		else
		{
			getPlugin().queuePlayer(gameType, player);
		}
		
		buildPage();
	}
	
	protected void OkClicked(Player player)
	{
		getPlugin().respondToInvite(player, true);
		
		buildPage();
	}
	
	protected void CancelClicked(Player player)
	{
		getPlugin().respondToInvite(player, false);
		
		player.closeInventory();
		System.out.println(this.getClass().getName() + " 191");
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
	
	private void drawCircle()
	{
		if (_circleIndex == 0)
		{
			setItem(3, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(15, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(41, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(29, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
		}
		else if (_circleIndex == 1)
		{
			setItem(4, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(24, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(40, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(20, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
		}
		else if (_circleIndex == 2)
		{
			setItem(5, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(33, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(39, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
			setItem(11, new ShopItem(Material.EYE_OF_ENDER, "Beep", 1, false));
		}
	}
}
