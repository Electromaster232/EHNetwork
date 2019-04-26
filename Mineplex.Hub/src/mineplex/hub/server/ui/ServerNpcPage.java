package mineplex.hub.server.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.SkullMeta;

import mineplex.core.account.CoreClientManager;
import mineplex.core.achievement.AchievementCategory;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilTime;
import mineplex.core.donation.DonationManager;
import mineplex.core.game.GameDisplay;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.server.ServerInfo;
import mineplex.hub.server.ServerManager;
import mineplex.hub.server.ServerSorter;
import mineplex.hub.server.ui.button.JoinServerButton;

public class ServerNpcPage extends ShopPageBase<ServerManager, ServerNpcShop> implements IServerPage
{
	// Shop Item Messages
	private static final String MESSAGE_BETA_GET_ULTRA = ChatColor.RESET + C.Line + "Get Ultra to join Tournament servers!";
	private static final String MESSAGE_JOIN = ChatColor.RESET + C.Line + "Click to Join";
	private static final String MESSAGE_IN_PROGRESS = ChatColor.RESET + C.Line + "Game in Progress.";
	private static final String MESSAGE_SPECTATE = ChatColor.RESET + C.Line + "Click to Spectate";
	private static final String MESSAGE_WAIT = ChatColor.RESET + C.Line + "and wait for next game!";
	private static final String MESSAGE_FULL_GET_ULTRA = ChatColor.RESET + C.Line + "Get Ultra to join full servers!";
	private static final String MESSAGE_RESTARTING = ChatColor.RESET + C.Line + "This server will be open shortly!";

	private String _serverNpcKey;
	private boolean _onMainPage = true;

	public ServerNpcPage(ServerManager plugin, ServerNpcShop shop, CoreClientManager clientManager,	DonationManager donationManager, String name, Player player, String serverNpcKey)
	{
		super(plugin, shop, clientManager, donationManager, name, player, 54);
		
		_serverNpcKey = serverNpcKey;

		buildPage();
	}

	@Override
	protected void buildPage()
	{
		List<ServerInfo> serverList = new ArrayList<ServerInfo>(getPlugin().GetServerList(_serverNpcKey));
		int slotsNeeded = 1;

		if (serverList.size() > 0)
		{
			slotsNeeded = getPlugin().GetRequiredSlots(getPlayer(), serverList.get(0).ServerType);
		}

		try
		{
			Collections.sort(serverList, new ServerSorter(slotsNeeded));
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}


		if (_onMainPage)
		{
			buildAvailableServerPage(serverList, slotsNeeded);
		}
		else
		{
			buildInProgressServerPage(serverList, slotsNeeded);
		}
	}

	private void showClock(long milliseconds, boolean beta)
	{
		int seconds = (int) (milliseconds / 1000);
		String timeLeft = UtilTime.convertString(milliseconds, 0, UtilTime.TimeUnit.FIT);

		byte data = (byte) (milliseconds - (seconds * 1000) > 500 ? 15 : 14);

		ShopItem item = new ShopItem(Material.WOOL, data,  C.cGreen + C.Bold + "Advertisement", null, new String[] {
				ChatColor.RESET + timeLeft + " Remaining...",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cGreen + "Do you love playing on Mineplex?",
				ChatColor.RESET + "If you do, please consider purchasing Ultra or Hero",
				ChatColor.RESET + "from the store! Money goes towards running servers",
				ChatColor.RESET + "and creating exciting new games for everyone!",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cYellow + "Requesting a Rank disables this advertisement.",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cGreen + "discord.gg/FttmSEQ"
		}, seconds, false, false);

		addItem(22, item);
	}

	private ShopItem buildShopItem(ServerInfo serverInfo, int slotsNeeded)
	{
		boolean ownsUltraPackage = getDonationManager().Get(getPlayer().getName()).OwnsUnknownPackage(serverInfo.ServerType + " ULTRA") || getClient().GetRank().Has(Rank.ULTRA);
		Material status = Material.REDSTONE_BLOCK;
		List<String> lore = new ArrayList<String>();

		String inProgress = (serverInfo.Game == null || serverInfo.ServerType.equalsIgnoreCase("Competitive")) ? MESSAGE_IN_PROGRESS : MESSAGE_SPECTATE;
		String wait = (serverInfo.Game == null || serverInfo.ServerType.equalsIgnoreCase("Competitive")) ? null : MESSAGE_WAIT;

		if (isStarting(serverInfo) && (serverInfo.MaxPlayers - serverInfo.CurrentPlayers) >= slotsNeeded)
			status = Material.EMERALD_BLOCK;
		else if (isInProgress(serverInfo))
			status = Material.GOLD_BLOCK;

		lore.add(ChatColor.RESET + "");

		if (serverInfo.Game != null)
			lore.add(ChatColor.RESET + C.cYellow + "Game: " + C.cWhite + serverInfo.Game);

		if (serverInfo.Map != null && !serverInfo.ServerType.equalsIgnoreCase("Competitive"))
				lore.add(ChatColor.RESET + C.cYellow + "Map: " + C.cWhite + serverInfo.Map);

		lore.add(ChatColor.RESET + C.cYellow + "Players: " + C.cWhite + serverInfo.CurrentPlayers + "/" + serverInfo.MaxPlayers);
		lore.add(ChatColor.RESET + "");
		lore.add(ChatColor.RESET + serverInfo.MOTD);

		if (serverInfo.Name.contains("T_") && !ownsUltraPackage)
		{
			lore.add(MESSAGE_BETA_GET_ULTRA);
		}
		else
		{
			if (isInProgress(serverInfo))
			{
				if (serverInfo.MOTD.contains("Restarting"))
				{
					status = Material.IRON_BLOCK;
					lore.add(MESSAGE_RESTARTING);
				}
				else
				{
					if (serverInfo.Game.equalsIgnoreCase("Survival Games"))
					{
						lore.add(ChatColor.RESET + C.Line + "Full Survival Games servers");
						lore.add(ChatColor.RESET + C.Line + "cannot be joined.");
					}
					else
					{
						if (!ownsUltraPackage)
						{
							lore.add(MESSAGE_FULL_GET_ULTRA);
						}
						else
						{
							lore.add(inProgress);
							if (wait != null)
								lore.add(wait);
						}
					}
				}
			}
			else
			{
				if (serverInfo.CurrentPlayers >= serverInfo.MaxPlayers && !ownsUltraPackage)
				{
					lore.add(MESSAGE_FULL_GET_ULTRA);
				}
				else if (!serverInfo.MOTD.contains("Open in"))
				{
					lore.add(MESSAGE_JOIN);
				}
			}
		}

		return new ShopItem(status, ChatColor.RESET + C.cGreen + C.Line + C.Bold + "Server " + serverInfo.Name.split("-")[1], lore.toArray(new String[lore.size()]), serverInfo.CurrentPlayers, false);
	}

	private void buildAvailableServerPage(List<ServerInfo> serverList, int slotsNeeded)
	{
		int serversToShow = 7;
		int greenCount = 0;
		int yellowCount = 0;
		int maxFull = 3;
		int greenStartSlot = 18 + ((9 - serversToShow) / 2);
		boolean showGreen = true;

		boolean beta = serverList.size() > 0 && serverList.get(0).Name.contains("BETA");
		boolean tournament = serverList.size() > 0 && serverList.get(0).Name.contains("T_");
		boolean privateServer = serverList.size() > 0 && serverList.get(0).ServerType.equals("Player");
		boolean ownsUltraPackage = getClient().GetRank().Has(Rank.ULTRA) || (serverList.size() > 0 && getDonationManager().Get(getPlayer().getName()).OwnsUnknownPackage(serverList.get(0).ServerType + " ULTRA"));

		long portalTime = getPlugin().getMillisecondsUntilPortal(getPlayer(), beta);
		if (portalTime > 0)
		{
			showClock(portalTime, beta);
			showGreen = false;
		}
		else if (tournament && !ownsUltraPackage)
		{
			ShopItem item = new ShopItem(Material.REDSTONE_BLOCK, (byte)0,  ChatColor.RESET + C.Bold + "Tournament Servers", null, new String[] {
					ChatColor.RESET + "",
					ChatColor.RESET + C.cAqua + "Ultra and Hero players have",
					ChatColor.RESET + C.cAqua + "access to our Tournament servers!",
					ChatColor.RESET + "",
					ChatColor.RESET + "",
					ChatColor.RESET + "Visit " + C.cGreen + "discord.gg/FttmSEQ" + C.cWhite + "!"
			}, 1, false, false);
	
			addItem(22, item);

			return;
		}
		else if (privateServer)
		{
			int staffSlot = 0;
			int slot = 18;
			for (ServerInfo serverInfo : serverList)
			{
				if (serverInfo.MOTD.contains("Private"))
					continue;

				if (serverInfo.MaxPlayers - serverInfo.CurrentPlayers <= 0)
					continue;

				if (serverInfo.HostedByStaff && staffSlot < 9)
				{
					addButton(staffSlot, getPrivateItem(serverInfo), new JoinServerButton(this, serverInfo));
					staffSlot++;
				}
				else
				{
					if (slot >= 54)
						continue;

					addButton(slot, getPrivateItem(serverInfo), new JoinServerButton(this, serverInfo));
					slot++;
				}
			}
			return;
		}
		
		int fullCount = 0;

		for (ServerInfo serverInfo : serverList)
		{
			int slot = greenCount + greenStartSlot;

			if (isStarting(serverInfo) && hasEnoughSlots(serverInfo, slotsNeeded) && greenCount < serversToShow)
			{
				if (showGreen)
				{
					boolean full = serverInfo.MaxPlayers - serverInfo.CurrentPlayers <= 0;
					
					if (full && fullCount >= maxFull)
						continue;
						
					ShopItem shopItem = buildShopItem(serverInfo, slotsNeeded);

					greenCount++;

					if (serverInfo.MOTD.contains("Open in"))
						setItem(slot, shopItem);
					else
						addButton(slot, shopItem, new JoinServerButton(this, serverInfo));
					
					if (full)
						fullCount++;
				}
			}
			else if (isInProgress(serverInfo))
			{
				yellowCount++;
			}
		}

		addButton(40, new ShopItem(Material.GOLD_BLOCK, C.cAqua + yellowCount + " Game" + (yellowCount == 1 ? "" : "s") + " In Progress", new String[]{MESSAGE_SPECTATE}, yellowCount > 64 ? 64 : yellowCount, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				_onMainPage = false;
			}
		});

		// Clear empty slots
		if (showGreen)
		{
			for (int i = greenCount + greenStartSlot; i < greenStartSlot + serversToShow; i++)
			{
				setItem(i, null);
			}
		}
	}

	private ShopItem getPrivateItem(ServerInfo serverInfo)
	{
		String hostName = serverInfo.Name.substring(0, serverInfo.Name.indexOf('-'));
		String server = ChatColor.GREEN + C.Bold + serverInfo.Name;
		String host = ChatColor.YELLOW + "Host: " + C.cWhite + hostName;
		Material material = Material.SKULL_ITEM;
		byte data = (byte) 3;

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(host);
		lore.add(" ");
		lore.add(ChatColor.RESET + C.cYellow + "Players: " + C.cWhite + serverInfo.CurrentPlayers + "/" + serverInfo.MaxPlayers);
		lore.add(" ");

		if (serverInfo.Game != null)
		{
			lore.add(ChatColor.RESET + C.cYellow + "Game: " + C.cWhite + serverInfo.Game);
			GameDisplay display = GameDisplay.matchName(serverInfo.Game);
			if (display != null)
			{
				material = display.getMaterial();
				data = display.getMaterialData();
			}
		}

		if (serverInfo.Map != null)
			lore.add(ChatColor.RESET + C.cYellow + "Map: " + C.cWhite + serverInfo.Map);

		if (serverInfo.HostedByStaff)
		{
			lore.add(" ");
			lore.add(ChatColor.RESET + C.cGreen + "Hosted by a Staff Member");
		}

		ShopItem shopItem = new ShopItem(material, data, server, lore.toArray(new String[0]), 1, false, false);
		if (material == Material.SKULL_ITEM)
		{
			SkullMeta meta = (SkullMeta) shopItem.getItemMeta();
			meta.setOwner(hostName);
			shopItem.setItemMeta(meta);
		}

		return shopItem;
	}

	private boolean isStarting(ServerInfo serverInfo)
	{
		return (serverInfo.MOTD.contains("Starting") || serverInfo.MOTD.contains("Recruiting") || serverInfo.MOTD.contains("Generating") || serverInfo.MOTD.contains("Waiting") || serverInfo.MOTD.contains("Open"));
	}

	private boolean isInProgress(ServerInfo serverInfo)
	{
		return serverInfo.MOTD.contains("Progress") || serverInfo.MOTD.contains("Restarting");
	}

	private boolean hasEnoughSlots(ServerInfo serverInfo, int slotsNeeded)
	{
		return (serverInfo.MaxPlayers - serverInfo.CurrentPlayers) >= slotsNeeded;
	}

	private void buildInProgressServerPage(List<ServerInfo> serverList, int slotsNeeded)
	{
		int slot = 9;

		for (ServerInfo serverInfo : serverList)
		{
			if (isInProgress(serverInfo) && slot < getSize())
			{
				ShopItem shopItem = buildShopItem(serverInfo, slotsNeeded);

				addButton(slot, shopItem, new JoinServerButton(this, serverInfo));

				slot++;
			}
		}

		addButton(4, new ShopItem(Material.BED, C.cGray + " \u21FD Go Back", new String[]{}, 1, false), new IButton()
		{
			@Override
			public void onClick(Player player, ClickType clickType)
			{
				clear();
				_onMainPage = true;
			}
		});

		while (slot < getSize())
		{
			if (getItem(slot) != null)
				setItem(slot, null);
			
			slot++;
		}
	}

	public void Update()
	{
		getButtonMap().clear();
		buildPage();
	}

	public void SelectServer(Player player, ServerInfo serverInfo)
	{		
		int slots = getPlugin().GetRequiredSlots(player, serverInfo.ServerType);
		
		if (serverInfo.MaxPlayers - serverInfo.CurrentPlayers < slots && !(getDonationManager().Get(getPlayer().getName()).OwnsUnknownPackage(serverInfo.ServerType + " ULTRA") || getClient().GetRank().Has(Rank.ULTRA)))
		{
			playDenySound(player);
			return;
		}
		
		getPlugin().SelectServer(player, serverInfo);
	}
}
