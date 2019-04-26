package mineplex.staffServer.customerSupport;

import java.util.HashSet;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.donation.DonationManager;
import mineplex.core.donation.Donor;
import mineplex.core.donation.repository.token.CoinTransactionToken;
import mineplex.core.donation.repository.token.TransactionToken;
import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomerSupport extends MiniPlugin
{
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private SalesPackageManager _salesPackageManager;
	
	private NautHashMap<Player, HashSet<String>> _agentCacheMap = new NautHashMap<Player, HashSet<String>>();

	public CustomerSupport(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, SalesPackageManager salesPackageManager)
	{
		super("Customer Support", plugin);

		_clientManager = clientManager;
		_donationManager = donationManager;
		_salesPackageManager = salesPackageManager;
	}

	@EventHandler
	public void Join(PlayerJoinEvent event)
	{
		if (!_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.MODERATOR))
		{
			event.getPlayer().kickPlayer("Only for staff.");
			return;
		}
		
		event.setJoinMessage(F.sys("Join", event.getPlayer().getName()));
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent event)
	{
		event.setQuitMessage(F.sys("Quit", event.getPlayer().getName()));
	}
	
	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;

		event.setFormat(C.cGold + "%1$s " + C.cWhite + "%2$s");
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new checkCommand(this));
	}

	public void Help(Player caller)
	{
		caller.sendMessage(F.main(getName(), "Usage : /check defek7"));
	}

	public void addAgentMapping(Player caller, String playerName)
	{
		if (!_agentCacheMap.containsKey(caller))
			_agentCacheMap.put(caller, new HashSet<String>());

		_agentCacheMap.get(caller).add(playerName);
	}

	public void showPlayerInfo(Player caller, String playerName)
	{
		CoreClient client = _clientManager.Get(playerName);
		Donor donor = _donationManager.Get(playerName);
		
		caller.sendMessage(C.cDGreen + C.Strike + "=============================================");
		caller.sendMessage(C.cBlue + "Name : " + C.cYellow + playerName);
		caller.sendMessage(C.cBlue + "Rank : " + C.cYellow + (client.GetRank() == null ? C.cRed + "Error rank null!" :  (client.GetRank().Name.isEmpty() ? "Regular" : client.GetRank().Name)));
		caller.sendMessage(C.cBlue + "Coins : " + C.cYellow + donor.getCoins());
		caller.sendMessage(C.cBlue + "Gems : " + C.cYellow + donor.GetGems());

		int enjinCoinsReceived = 0;		
		int oldChestsReceived = 0;
		int ancientChestsReceived = 0;
		int mythicalChestsReceived = 0;
		
		for (CoinTransactionToken transaction : donor.getCoinTransactions())
		{
			if (transaction.Source.equalsIgnoreCase("Poll") || transaction.Source.equalsIgnoreCase("Halloween Pumpkin") || transaction.Source.equalsIgnoreCase("Treasure Chest") || transaction.Source.equalsIgnoreCase("Coin Party Bomb Pickup") || transaction.Source.contains("Reward") || transaction.Source.contains("purchase"))
			{
				if (transaction.Source.contains("purchase"))
					enjinCoinsReceived += transaction.Amount;
			}
		}

		for (TransactionToken transaction : donor.getTransactions())
		{
			if (transaction.SalesPackageName.startsWith("Old Chest"))
			{
				if (transaction.Coins == 0 && transaction.Gems == 0)
				{
					if (transaction.SalesPackageName.split(" ").length == 3)
						oldChestsReceived += Integer.parseInt(transaction.SalesPackageName.split(" ")[2]);
					else if (transaction.SalesPackageName.split(" ").length == 2)
						oldChestsReceived += 1;
				}
					
			}
			if (transaction.SalesPackageName.startsWith("Ancient Chest"))
			{
				if (transaction.Coins == 0 && transaction.Gems == 0)
				{
					if (transaction.SalesPackageName.split(" ").length == 3)
						ancientChestsReceived += Integer.parseInt(transaction.SalesPackageName.split(" ")[2]);
					else if (transaction.SalesPackageName.split(" ").length == 2)
						ancientChestsReceived += 1;
				}
					
			}
			if (transaction.SalesPackageName.startsWith("Mythical Chest"))
			{
				if (transaction.Coins == 0 && transaction.Gems == 0)
				{
					if (transaction.SalesPackageName.split(" ").length == 3)
						mythicalChestsReceived += Integer.parseInt(transaction.SalesPackageName.split(" ")[2]);
					else if (transaction.SalesPackageName.split(" ").length == 2)
						mythicalChestsReceived += 1;
				}
					
			}
		}
		
		caller.sendMessage(C.cBlue + "Enjin Coin Total Received : " + C.cYellow + enjinCoinsReceived);
		caller.sendMessage(C.cBlue + "Old Chests Received  : " + C.cYellow + oldChestsReceived);
		caller.sendMessage(C.cBlue + "Ancient Chests Received  : " + C.cYellow + ancientChestsReceived);
		caller.sendMessage(C.cBlue + "Mythical Chests Received  : " + C.cYellow + mythicalChestsReceived);
		
		caller.sendMessage(C.cDGreen + C.Strike + "=============================================");
		_salesPackageManager.displaySalesPackages(caller, playerName);
		caller.sendMessage(C.cDGreen + C.Strike + "=============================================");
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event)
	{
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler
	public void removeMapping(PlayerQuitEvent event)
	{
		_agentCacheMap.remove(event.getPlayer());
	}
	
	@EventHandler
	public void foodLevelChange(FoodLevelChangeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void entityDeath(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.VOID)
			event.getEntity().teleport(event.getEntity().getWorld().getSpawnLocation());
		
		event.setCancelled(true);
	}
}
