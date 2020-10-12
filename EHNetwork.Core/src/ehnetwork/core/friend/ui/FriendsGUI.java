package ehnetwork.core.friend.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import ehnetwork.core.command.CommandCenter;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.friend.FriendManager;
import ehnetwork.core.friend.FriendStatusType;
import ehnetwork.core.friend.data.FriendData;
import ehnetwork.core.friend.data.FriendStatus;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.core.itemstack.ItemLayout;
import ehnetwork.core.shop.item.IButton;

public class FriendsGUI implements Listener
{
	private NautHashMap<Integer, IButton> _buttonMap = new NautHashMap<Integer, IButton>();
	private FriendPage _currentPage = FriendPage.FRIENDS;
	private FriendPage _previousPage;
	private Player _player;
	private FriendManager _plugin;
	private Inventory _inventory;
	private int _page;
	private Comparator<FriendStatus> _friendCompare = new Comparator<FriendStatus>()
	{

		@Override
		public int compare(FriendStatus o1, FriendStatus o2)
		{
			if (o1.Online == o2.Online)
			{
				return o1.Name.compareToIgnoreCase(o2.Name);
			}

			if (o1.Online)
			{
				return -1;
			}
			return 1;
		}
	};

	public FriendsGUI(FriendManager plugin, Player player)
	{
		_plugin = plugin;
		_player = player;

		buildPage();

		_plugin.registerEvents(this);

		getFriendData().setGui(this);
	}

	private void AddButton(int slot, ItemStack item, IButton button)
	{
		_inventory.setItem(slot, item);
		_buttonMap.put(slot, button);
	}

	public void buildDeleteFriends()
	{

		ArrayList<FriendStatus> friends = new ArrayList<FriendStatus>();

		for (FriendStatus friend : getFriendData().getFriends())
		{
			if (friend.Status == FriendStatusType.Accepted)
			{
				friends.add(friend);
			}
		}

		Collections.sort(friends, _friendCompare);

		boolean pages = this.addPages(friends.size(), new Runnable()
		{
			public void run()
			{
				buildDeleteFriends();
			}
		});

		for (int i = 0; i < (pages ? 27 : 36); i++)
		{
			int friendSlot = (_page * 27) + i;
			int slot = i + 18;

			if (friendSlot >= friends.size())
			{
				ItemStack item = _inventory.getItem(slot);

				if (item == null || item.getType() == Material.AIR)
				{
					break;
				}
				else
				{
					_inventory.setItem(slot, new ItemStack(Material.AIR));

					continue;
				}
			}

			FriendStatus friend = friends.get(friendSlot);

			ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) (friend.Online ? 3 : 0));

			builder.setTitle(C.cWhite + C.Bold + friend.Name);
			builder.setPlayerHead(friend.Name);

			builder.addLore(C.cGray + C.Bold + "Status: " + (friend.Online ? C.cDGreen + "Online" : C.cRed + "Offline"));

			if (friend.Online)
			{
				builder.addLore(C.cGray + C.Bold + "Server: " + C.cYellow + friend.ServerName);
			}
			else
			{
				builder.addLore(C.cGray + "Last seen " + UtilTime.MakeStr(friend.LastSeenOnline) + " ago");
			}

			builder.addLore("");

			builder.addLore(C.cGray + "Left click to delete from friends");

			final String name = friend.Name;

			AddButton(slot, builder.build(), new IButton()
			{

				@Override
				public void onClick(Player player, ClickType clickType)
				{
					if (clickType.isLeftClick())
					{
						CommandCenter.Instance.OnPlayerCommandPreprocess(new PlayerCommandPreprocessEvent(player, "/unfriend "
								+ name));
					}
				}
			});
		}
	}

	private boolean addPages(int amount, final Runnable runnable)
	{
		if (amount > 36)
		{
			if (_page > 0)
			{
				AddButton(45, new ItemBuilder(Material.SIGN).setTitle("Previous Page").build(), new IButton()
				{

					@Override
					public void onClick(Player player, ClickType clickType)
					{
						Iterator<Integer> itel = _buttonMap.keySet().iterator();

						while (itel.hasNext())
						{
							int slot = itel.next();
							if (slot > 8)
							{
								itel.remove();
								_inventory.setItem(slot, new ItemStack(Material.AIR));
							}
						}

						_page -= 1;
						runnable.run();
					}
				});
			}

			if (amount > (_page + 1) * 27)
			{
				AddButton(53, new ItemBuilder(Material.SIGN).setTitle("Next Page").build(), new IButton()
				{

					@Override
					public void onClick(Player player, ClickType clickType)
					{
						Iterator<Integer> itel = _buttonMap.keySet().iterator();

						while (itel.hasNext())
						{
							int slot = itel.next();
							if (slot > 8)
							{
								itel.remove();
								_inventory.setItem(slot, new ItemStack(Material.AIR));
							}
						}

						_page += 1;
						runnable.run();
					}
				});
			}

			return true;
		}
		return false;
	}

	private void buildFriends()
	{
		ArrayList<FriendStatus> friends = new ArrayList<FriendStatus>();

		for (FriendStatus friend : getFriendData().getFriends())
		{
			if (friend.Status == FriendStatusType.Accepted)
			{
				friends.add(friend);
			}
		}

		Collections.sort(friends, _friendCompare);
		boolean pages = addPages(friends.size(), new Runnable()
		{

			@Override
			public void run()
			{
				buildFriends();
			}
		});

		for (int i = 0; i < (pages ? 27 : 36); i++)
		{
			int friendSlot = (_page * 27) + i;
			int slot = i + 18;

			if (friendSlot >= friends.size())
			{
				ItemStack item = _inventory.getItem(slot);

				if (item == null || item.getType() == Material.AIR)
				{
					break;
				}
				else
				{
					_inventory.setItem(slot, new ItemStack(Material.AIR));

					continue;
				}
			}

			FriendStatus friend = friends.get(friendSlot);

			ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) (friend.Online ? 3 : 0));

			builder.setTitle(C.cWhite + C.Bold + friend.Name);
			builder.setPlayerHead(friend.Name);

			builder.addLore(C.cGray + C.Bold + "Status: " + (friend.Online ? C.cDGreen + "Online" : C.cRed + "Offline"));

			if (friend.Online)
			{
				builder.addLore(C.cGray + C.Bold + "Server: " + C.cYellow + friend.ServerName);
			}
			else
			{
				builder.addLore(C.cGray + "Last seen " + UtilTime.MakeStr(friend.LastSeenOnline) + " ago");
			}

			if (friend.Online)
			{
				builder.addLore("");
				builder.addLore(C.cGray + "Left click to teleport to their server");

				final String serverName = friend.ServerName;

				AddButton(slot, builder.build(), new IButton()
				{

					@Override
					public void onClick(Player player, ClickType clickType)
					{
						_plugin.getPortal().sendPlayerToServer(player, serverName);
					}
				});
			}
			else
			{
				_inventory.setItem(slot, builder.build());
			}
		}
	}

	public void updateGui()
	{
		if (_currentPage == FriendPage.FRIENDS)
		{
			buildFriends();
		}
		else if (_currentPage == FriendPage.FRIEND_REQUESTS)
		{
			buildRequests();
		}
		else if (_currentPage == FriendPage.DELETE_FRIENDS)
		{
			buildDeleteFriends();
		}
	}

	private void buildPage()
	{

		if (_currentPage != _previousPage)
		{
			_inventory = Bukkit.createInventory(null, 54, _currentPage.getName());
		}
		else
		{
			_inventory.setItem(53, new ItemStack(Material.AIR));
			_inventory.setItem(45, new ItemStack(Material.AIR));
		}

		_page = 0;
		_buttonMap.clear();

		ArrayList<Integer> itemSlots = new ItemLayout("OXOXOXOXO").getItemSlots();

		for (int i = 0; i < FriendPage.values().length; i++)
		{
			final FriendPage page = FriendPage.values()[i];

			ItemStack icon = page == _currentPage ?

			new ItemBuilder(page.getIcon()).addEnchantment(UtilInv.getDullEnchantment(), 1).build()

			:

			page.getIcon();

			this.AddButton(itemSlots.get(i), icon, new IButton()
			{

				@Override
				public void onClick(Player player, ClickType clickType)
				{
					if (_currentPage != page || _page != 0)
					{
						_currentPage = page;
						buildPage();
					}
				}

			});
		}

		if (_currentPage == FriendPage.FRIENDS)
		{
			buildFriends();
		}
		else if (_currentPage == FriendPage.FRIEND_REQUESTS)
		{
			buildRequests();
		}
		else if (_currentPage == FriendPage.DELETE_FRIENDS)
		{
			buildDeleteFriends();
		}
		else if (_currentPage == FriendPage.SEND_REQUEST)
		{
			unregisterListener();

			new AddFriendPage(_plugin, _player);

			return;
		}
		else if (_currentPage == FriendPage.TOGGLE_DISPLAY)
		{
			_player.closeInventory();

			CommandCenter.Instance.OnPlayerCommandPreprocess(new PlayerCommandPreprocessEvent(_player, "/friendsdisplay"));

			return;
		}

		if (_previousPage != _currentPage)
		{
			_previousPage = _currentPage;

			EntityPlayer nmsPlayer = ((CraftPlayer) _player).getHandle();
			if (nmsPlayer.activeContainer != nmsPlayer.defaultContainer)
			{
				// Do this so that other inventories know their time is over.
				CraftEventFactory.handleInventoryCloseEvent(nmsPlayer);
				nmsPlayer.m();
			}

			_player.openInventory(_inventory);
		}
	}

	private void buildRequests()
	{
		ArrayList<FriendStatus> friends = new ArrayList<FriendStatus>();

		for (FriendStatus friend : getFriendData().getFriends())
		{
			if (friend.Status == FriendStatusType.Sent || friend.Status == FriendStatusType.Pending)
			{
				friends.add(friend);
			}
		}

		Collections.sort(friends, new Comparator<FriendStatus>()
		{

			@Override
			public int compare(FriendStatus o1, FriendStatus o2)
			{
				if (o1.Status == o2.Status)
				{
					return o1.Name.compareToIgnoreCase(o2.Name);
				}

				if (o1.Status == FriendStatusType.Sent)
				{
					return -1;
				}

				return 1;
			}
		});

		boolean pages = addPages(friends.size(), new Runnable()
		{

			@Override
			public void run()
			{
				buildRequests();
			}
		});

		for (int i = 0; i < (pages ? 27 : 36); i++)
		{
			int friendSlot = (_page * 27) + i;
			final int slot = i + 18;

			if (friendSlot >= friends.size())
			{
				ItemStack item = _inventory.getItem(slot);

				if (item == null || item.getType() == Material.AIR)
				{
					break;
				}
				else
				{
					_inventory.setItem(slot, new ItemStack(Material.AIR));

					continue;
				}
			}

			FriendStatus friend = friends.get(friendSlot);

			ItemBuilder builder;

			final boolean isSender = friend.Status == FriendStatusType.Sent;

			if (isSender)
			{
				builder = new ItemBuilder(Material.ENDER_PEARL);

				builder.setTitle(C.cGray + "Friend request to " + C.cWhite + C.Bold + friend.Name);
			}
			else
			{
				builder = new ItemBuilder(Material.PAPER);

				builder.setTitle(C.cGray + "Friend request from " + C.cWhite + C.Bold + friend.Name);
			}

			builder.addLore("");

			builder.addLore(C.cGray + (isSender ? "C" : "Left c") + "lick to " + (isSender ? "cancel" : "accept")
					+ " friend request");

			if (!isSender)
			{
				builder.addLore(C.cGray + "Right click to refuse friend request");
			}

			final String name = friend.Name;

			AddButton(slot, builder.build(), new IButton()
			{

				@Override
				public void onClick(Player player, ClickType clickType)
				{
					if (isSender || clickType.isRightClick())
					{
						_plugin.removeFriend(_player, name);
//                        CommandCenter.Instance.OnPlayerCommandPreprocess(new PlayerCommandPreprocessEvent(player, "/unfriend "
//                                + name));

						_inventory.setItem(slot, new ItemStack(Material.AIR));
						_buttonMap.remove(slot);
					}
					else if (!isSender && clickType.isLeftClick())
					{
						_plugin.addFriend(_player, name);
//                        CommandCenter.Instance.OnPlayerCommandPreprocess(new PlayerCommandPreprocessEvent(player, "/friend "
//                                + name));

						_inventory.setItem(slot, new ItemStack(Material.AIR));
						_buttonMap.remove(slot);
					}
				}
			});
		}
	}

	private FriendData getFriendData()
	{
		return _plugin.Get(_player);
	}

	@EventHandler
	public void OnInventoryClick(InventoryClickEvent event)
	{
		if (_inventory.getTitle().equals(event.getInventory().getTitle()) && event.getWhoClicked() == _player)
		{
			if (_buttonMap.containsKey(event.getRawSlot()))
			{
				if (event.getWhoClicked() instanceof Player)
				{
					IButton button = _buttonMap.get(event.getRawSlot());

					button.onClick(((Player) event.getWhoClicked()), event.getClick());

					_player.playSound(_player.getLocation(), Sound.NOTE_PLING, 1, 1.6f);
				}
			}
			else
			{

				_player.playSound(_player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void OnInventoryClose(InventoryCloseEvent event)
	{
		if (_inventory.getTitle().equals(event.getInventory().getTitle()) && event.getPlayer() == _player)
		{
			unregisterListener();
		}
	}

	private void unregisterListener()
	{
		FriendData data = getFriendData();

		if (data != null && data.getGui() == this)
		{
			data.setGui(null);
		}

		HandlerList.unregisterAll(this);
	}
}
