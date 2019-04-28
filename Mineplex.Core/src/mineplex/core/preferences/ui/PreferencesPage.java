package mineplex.core.preferences.ui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.donation.DonationManager;
import mineplex.core.preferences.PreferencesManager;
import mineplex.core.preferences.UserPreferences;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;

public class PreferencesPage extends ShopPageBase<PreferencesManager, PreferencesShop>
{
	private IButton _toggleHubGames;
	private IButton _toggleHubPlayers;
	private IButton _toggleChat;
	private IButton _togglePrivateChat;
	private IButton _toggleHubPartyRequests;
	private IButton _togglePendingFriendRequests;
	private IButton _toggleHubInvisibility;
	private IButton _toggleHubForcefield;
	private IButton _toggleHubIgnoreVelocity;
	private IButton _toggleMacReports;

	private boolean _hubGamesToggled;
	private boolean _hubPlayersToggled;
	private boolean _hubChatToggled;
	private boolean _hubPrivateChatToggled;
	private boolean _hubPartyRequestsToggled;
	private boolean _pendingFriendRequestsToggled;
	private boolean _hubInvisibilityToggled;
	private boolean _hubForcefieldToggled;
	private boolean _macReportsToggled;
	private boolean _hubIgnoreVelocityToggled;
	
	public PreferencesPage(PreferencesManager plugin, PreferencesShop shop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
	{
		super(plugin, shop, clientManager, donationManager, name, player, 54);
		
		createButtons();		
		buildPage();
	}

	private void createButtons()
	{
		_toggleHubGames = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubGames(player);
			}
		};
		
		_toggleHubPlayers = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubPlayers(player);
			}
		};
		
		_toggleChat = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleChat(player);
			}
		};
		
		_togglePrivateChat = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				togglePrivateChat(player);
			}
		};
		
		_toggleHubPartyRequests = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubPartyRequests(player);
			}
		};
		
		_togglePendingFriendRequests = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				togglePendingFriendRequests(player);
			}
		};
		
		_toggleHubInvisibility = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubInvisibility(player);
			}
		};
		
		_toggleHubForcefield = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubForcefield(player);
			}
		};

		_toggleMacReports = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleMacReports(player);
			}
		};

		_toggleHubIgnoreVelocity = new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				toggleHubIgnoreVelocity(player);
			}
		};
	}
	
	private void buildPreference(int index, Material material, String name, boolean preference, IButton button)
	{
		buildPreference(index, material, (byte)0, name, preference, button);
	}

	private void buildPreference(int index, Material material, byte data, String name, boolean preference, IButton button)
	{
		String[] description = new String[] {
				"" + (preference ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"), 
				ChatColor.RED + " ", 
				ChatColor.RESET + "Click to " + (preference ? "Disable" : "Enable") };
		
		addButton(index, new ShopItem(material, data, (preference ? ChatColor.GREEN : ChatColor.RED) + name, description, 1, false, false), button);
		addButton(index + 9, new ShopItem(Material.INK_SACK, (preference ? (byte) 10 : (byte) 8), (preference ? ChatColor.GREEN : ChatColor.RED) + name, description, 1, false, false), button);
	}
	
	protected void toggleHubForcefield(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).HubForcefield = !getPlugin().Get(player).HubForcefield;
		_hubForcefieldToggled = !_hubForcefieldToggled;
		buildPage();
	}

	protected void toggleHubInvisibility(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).Invisibility = !getPlugin().Get(player).Invisibility;
		_hubInvisibilityToggled = !_hubInvisibilityToggled;
		buildPage();
	}

	protected void toggleHubPartyRequests(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).PartyRequests = !getPlugin().Get(player).PartyRequests;
		_hubPartyRequestsToggled = !_hubPartyRequestsToggled;
		buildPage();
	}
	
	protected void togglePendingFriendRequests(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).PendingFriendRequests = !getPlugin().Get(player).PendingFriendRequests;
		_pendingFriendRequestsToggled = !_pendingFriendRequestsToggled;
		buildPage();
	}

	protected void togglePrivateChat(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).PrivateMessaging = !getPlugin().Get(player).PrivateMessaging;
		_hubPrivateChatToggled = !_hubPrivateChatToggled;
		buildPage();
	}

	protected void toggleChat(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).ShowChat = !getPlugin().Get(player).ShowChat;
		_hubChatToggled = !_hubChatToggled;
		buildPage();
	}

	protected void toggleHubPlayers(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).ShowPlayers = !getPlugin().Get(player).ShowPlayers;
		_hubPlayersToggled = !_hubPlayersToggled;
		buildPage();
	}

	protected void toggleHubGames(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).HubGames = !getPlugin().Get(player).HubGames;
		_hubGamesToggled = !_hubGamesToggled;
		buildPage();
	}

	protected void toggleMacReports(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).ShowMacReports = !getPlugin().Get(player).ShowMacReports;
		_macReportsToggled = !_macReportsToggled;
		buildPage();
	}

	protected void toggleHubIgnoreVelocity(org.bukkit.entity.Player player)
	{
		getPlugin().Get(player).IgnoreVelocity = !getPlugin().Get(player).IgnoreVelocity;
		_hubIgnoreVelocityToggled = !_hubIgnoreVelocityToggled;
		buildPage();
	}
	
	@Override
	public void playerClosed()
	{
		super.playerClosed();
		if (preferencesChanged())
		{
			getPlugin().savePreferences(getPlayer());
		}
	}

	@Override
	protected void buildPage()
	{
		clear();
		
		UserPreferences userPreferences = getPlugin().Get(getPlayer());
		int index = 9;
		
		buildPreference(index, Material.FIREBALL, "Hub Stacker", userPreferences.HubGames, _toggleHubGames);
		index += 2;
		buildPreference(index, Material.EYE_OF_ENDER, "Hub Player Visibility", userPreferences.ShowPlayers, _toggleHubPlayers);
		index += 2;
		buildPreference(index, Material.PAPER, "Player Chat", userPreferences.ShowChat, _toggleChat);
		index += 2;
		buildPreference(index, Material.EMPTY_MAP, "Private Messaging", userPreferences.PrivateMessaging, _togglePrivateChat);
		index += 2;
		buildPreference(index, Material.SKULL_ITEM, (byte)3, "Hub Party Requests", userPreferences.PartyRequests, _toggleHubPartyRequests);		
		
		buildPreference(40, Material.RED_ROSE, "Show Pending Friend Requests", userPreferences.PendingFriendRequests, _togglePendingFriendRequests);
		
		if (getClientManager().Get(getPlayer()).GetRank() == Rank.YOUTUBE || getClientManager().Get(getPlayer()).GetRank() == Rank.ANNOYING_KID || getClientManager().Get(getPlayer()).GetRank() == Rank.TWITCH)
		{
			buildPreference(38, Material.NETHER_STAR, "Hub Invisibility", userPreferences.Invisibility, _toggleHubInvisibility);
			buildPreference(42, Material.SLIME_BALL, "Hub Forcefield", userPreferences.HubForcefield, _toggleHubForcefield);
			buildPreference(44, Material.SADDLE, "Hub Ignore Velocity", userPreferences.IgnoreVelocity, _toggleHubIgnoreVelocity);
		}
		if (getClientManager().Get(getPlayer()).GetRank().Has(Rank.ADMIN) || getClientManager().Get(getPlayer()).GetRank() == Rank.JNR_DEV)
		{
			buildPreference(36, Material.NETHER_STAR, "Hub Invisibility", userPreferences.Invisibility, _toggleHubInvisibility);
			buildPreference(38, Material.SLIME_BALL, "Hub Forcefield", userPreferences.HubForcefield, _toggleHubForcefield);
			buildPreference(42, Material.PAPER, "Mac Reports", userPreferences.ShowMacReports, _toggleMacReports);
			buildPreference(44, Material.SADDLE, "Hub Ignore Velocity", userPreferences.IgnoreVelocity, _toggleHubIgnoreVelocity);
		}
		else if (getClientManager().Get(getPlayer()).GetRank().Has(Rank.MODERATOR))
		{
			buildPreference(38, Material.PAPER, "Mac Reports", userPreferences.ShowMacReports, _toggleMacReports);
			buildPreference(42, Material.SADDLE, "Hub Ignore Velocity", userPreferences.IgnoreVelocity, _toggleHubIgnoreVelocity);
		}
	}
	
	public boolean preferencesChanged()
	{
		return 	_hubGamesToggled || _hubPlayersToggled || _hubChatToggled || _hubPrivateChatToggled || _hubPartyRequestsToggled || _hubInvisibilityToggled || _hubForcefieldToggled || _pendingFriendRequestsToggled;
	}
}