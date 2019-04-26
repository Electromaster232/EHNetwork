package mineplex.hub.server.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.server.LobbySorter;
import mineplex.hub.server.ServerInfo;
import mineplex.hub.server.ServerManager;
import mineplex.hub.server.ui.button.JoinServerButton;

public class LobbyMenu extends ShopPageBase<ServerManager, LobbyShop> implements IServerPage
{
	private String _serverGroup;
	
	public LobbyMenu(ServerManager plugin, LobbyShop lobbyShop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player, String serverGroup)
	{
		super(plugin, lobbyShop, clientManager, donationManager, name, player, 54);

		_serverGroup = serverGroup;
		
		buildPage();
	}

	@Override
	protected void buildPage()
	{
		List<ServerInfo> serverList = new ArrayList<ServerInfo>(getPlugin().GetServerList(_serverGroup));

		try
		{
			Collections.sort(serverList, new LobbySorter());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		int slot = 0;	
		String openFull = ChatColor.RESET + C.Line + "Get Ultra to join full servers!";
		String openFullUltra = ChatColor.RESET + C.Line + "Click to join!";
		
		for (ServerInfo serverInfo : serverList)
		{
			Material status = Material.IRON_BLOCK;
			List<String> lore = new ArrayList<String>();
			
			slot = Integer.parseInt(serverInfo.Name.split("-")[1]) - 1;
			if (slot >= 54)
				continue;
			
			if (serverInfo.Name.equalsIgnoreCase(getPlugin().getStatusManager().getCurrentServerName()))
				status = Material.EMERALD_BLOCK;
			
			lore.add(ChatColor.RESET + "");
			lore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "Players: " + ChatColor.WHITE + serverInfo.CurrentPlayers + "/" + serverInfo.MaxPlayers);
			lore.add(ChatColor.RESET + "");
			
			if (serverInfo.CurrentPlayers >= serverInfo.MaxPlayers)
			{
				if (!getClient().GetRank().Has(Rank.ULTRA))
					lore.add(openFull);
				else
					lore.add(openFullUltra);
			}
			else
				lore.add(ChatColor.RESET + C.Line + "Click to join!");
			
			if (status != Material.EMERALD_BLOCK)
				addButton(slot, new ShopItem(status, ChatColor.UNDERLINE + "" + ChatColor.BOLD + "" + ChatColor.WHITE + "Server " + serverInfo.Name.substring(serverInfo.Name.indexOf('-') + 1), lore.toArray(new String[lore.size()]), Integer.parseInt(serverInfo.Name.substring(serverInfo.Name.indexOf('-') + 1)), false), new JoinServerButton(this, serverInfo));
			else
				addItem(slot, new ShopItem(status, ChatColor.UNDERLINE + "" + ChatColor.BOLD + "" + ChatColor.WHITE + "Server " + serverInfo.Name.substring(serverInfo.Name.indexOf('-') + 1), lore.toArray(new String[lore.size()]), Integer.parseInt(serverInfo.Name.substring(serverInfo.Name.indexOf('-') + 1)), false));
		}
		
		while (slot < 54)
		{
			clear(slot);
			slot++;
		}
	}

	public void Update()
	{	
		clear();
		getButtonMap().clear();
		buildPage();
	}

	@Override
	public void SelectServer(org.bukkit.entity.Player player, ServerInfo serverInfo)
	{
		int slots = getPlugin().GetRequiredSlots(player, serverInfo.ServerType);
		
		if (serverInfo.MaxPlayers - serverInfo.CurrentPlayers < slots)
		{
			playDenySound(player);
			return;
		}
		
		getPlugin().SelectServer(player, serverInfo);
	}
}
