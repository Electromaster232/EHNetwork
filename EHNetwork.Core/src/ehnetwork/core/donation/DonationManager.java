package ehnetwork.core.donation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniDbClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.event.ClientWebResponseEvent;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.donation.command.CoinCommand;
import ehnetwork.core.donation.command.GemCommand;
import ehnetwork.core.donation.command.GoldCommand;
import ehnetwork.core.donation.repository.DonationRepository;
import ehnetwork.core.donation.repository.token.DonorTokenWrapper;
import ehnetwork.core.server.util.TransactionResponse;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class DonationManager extends MiniDbClientPlugin<Donor>
{
	private DonationRepository _repository;
	
	private NautHashMap<Player, NautHashMap<String, Integer>> _gemQueue = new NautHashMap<Player, NautHashMap<String, Integer>>();
	private NautHashMap<Player, NautHashMap<String, Integer>> _coinQueue = new NautHashMap<Player, NautHashMap<String, Integer>>();
	private NautHashMap<Player, NautHashMap<String, Integer>> _goldQueue = new NautHashMap<Player, NautHashMap<String, Integer>>();
	
	public DonationManager(JavaPlugin plugin, CoreClientManager clientManager, String webAddress)
	{
		super("Donation", plugin, clientManager);
		
		_repository = new DonationRepository(plugin, webAddress);
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new GemCommand(this));
		addCommand(new CoinCommand(this));
		addCommand(new GoldCommand(this));
	}
	
	@EventHandler
	public void OnClientWebResponse(ClientWebResponseEvent event)
	{
		DonorTokenWrapper token = new Gson().fromJson(event.GetResponse(), DonorTokenWrapper.class);
		LoadDonor(token);//, event.getUniqueId());
	}

	private void LoadDonor(DonorTokenWrapper token)//, UUID uuid)
	{
		Get(token.Name).loadToken(token.DonorToken);
		//_repository.updateGemsAndCoins(uuid, Get(token.Name).GetGems(), Get(token.Name).getCoins());
	}
	
	public void PurchaseUnknownSalesPackage(final Callback<TransactionResponse> callback, final String name, final int accountId, final String packageName, final boolean coinPurchase, final int cost, boolean oneTimePurchase)
	{
		final Donor donor = Bukkit.getPlayerExact(name) != null ? Get(name) : null;
		
		if (donor != null)
		{
			if (oneTimePurchase && donor.OwnsUnknownPackage(packageName))
			{
				if (callback != null)
					callback.run(TransactionResponse.AlreadyOwns);
				
				return;
			}
		}
		
		_repository.PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
		{
			public void run(TransactionResponse response)
			{
				if (response == TransactionResponse.Success)
				{
					if (donor != null)
					{
						donor.AddUnknownSalesPackagesOwned(packageName);
						donor.DeductCost(cost, coinPurchase ? CurrencyType.Coins : CurrencyType.Gems);
					}
				}
				
				if (callback != null)
					callback.run(response);
			}
		}, name, accountId, packageName, coinPurchase, cost);
	}
	
	public void PurchaseKnownSalesPackage(final Callback<TransactionResponse> callback, final String name, final UUID uuid, final int cost, final int salesPackageId) 
	{
		_repository.PurchaseKnownSalesPackage(new Callback<TransactionResponse>()
		{
			public void run(TransactionResponse response)
			{
				if (response == TransactionResponse.Success)
				{
					Donor donor = Get(name);				

					if (donor != null)
					{
						donor.AddSalesPackagesOwned(salesPackageId);
					}
				}
				
				if (callback != null)
					callback.run(response);
			}
		}, name, uuid.toString(), cost, salesPackageId);
	}
	
	public void RewardGems(Callback<Boolean> callback, String caller, String name, UUID uuid, int amount)
	{
		RewardGems(callback, caller, name, uuid, amount, true);
	}

	public void RewardGems(final Callback<Boolean> callback, final String caller, final String name, final UUID uuid, final int amount, final boolean updateTotal)
	{
		_repository.gemReward(new Callback<Boolean>()
		{
			public void run(Boolean success)
			{
				if (success)
				{
					if (updateTotal)
					{
						Donor donor = Get(name);				
						
						if (donor != null)
						{
							donor.AddGems(amount);
						}
					}
				}
				
				if (callback != null)
					callback.run(success);
			}
		}, caller, name, uuid.toString(), amount);
	}
	
	public void RewardGemsLater(final String caller, final Player player, final int amount)
	{
		if (!_gemQueue.containsKey(player))
			_gemQueue.put(player, new NautHashMap<String, Integer>());
		
		int totalAmount = amount;
		
		if (_gemQueue.get(player).containsKey(caller))
			totalAmount += _gemQueue.get(player).get(caller);
		
		_gemQueue.get(player).put(caller, totalAmount);
		
		//Do Temp Change
		Donor donor = Get(player.getName());				
		
		if (donor != null)
			donor.AddGems(amount);
	}
	
	@EventHandler
	public void UpdateGemQueue(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWER)
			return;
		
		for (Player player : _gemQueue.keySet())
		{
			String caller = null;
			int total = 0;
			
			for (String curCaller : _gemQueue.get(player).keySet())
			{
				caller = curCaller;
				total += _gemQueue.get(player).get(curCaller);
			}
			
			if (caller == null)
				continue;

			//Actually Add Gems
			RewardGems(null, caller, player.getName(), player.getUniqueId(), total, false);
			
			System.out.println("Queue Added [" + player + "] with Gems [" + total + "] for [" + caller + "]");
			
			//Clean
			_gemQueue.get(player).clear();
		}
		
		//Clean
		_gemQueue.clear();
	}

	public void RewardCoins(Callback<Boolean> callback, String caller, String name, int accountId, int amount)
	{
		RewardCoins(callback, caller, name, accountId, amount, true);
	}

	public void RewardCoins(final Callback<Boolean> callback, final String caller, final String name, final int accountId, final int amount, final boolean updateTotal)
	{
		_repository.rewardCoins(new Callback<Boolean>()
		{
			public void run(Boolean success)
			{
				if (success)
				{
					if (updateTotal)
					{
						Donor donor = Get(name);				
						
						if (donor != null)
						{
							donor.addCoins(amount);
						}
					}
				}
				
				if (callback != null)
					callback.run(success);
			}
		}, caller, name, accountId, amount);
	}
	
	public void RewardCoinsLater(final String caller, final Player player, final int amount)
	{
		if (!_coinQueue.containsKey(player))
			_coinQueue.put(player, new NautHashMap<String, Integer>());
		
		int totalAmount = amount;
		
		if (_coinQueue.get(player).containsKey(caller))
			totalAmount += _coinQueue.get(player).get(caller);
		
		_coinQueue.get(player).put(caller, totalAmount);
		
		//Do Temp Change
		Donor donor = Get(player.getName());				
		
		if (donor != null)
			donor.addCoins(amount);
	}
	
	@EventHandler
	public void UpdateCoinQueue(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWER)
			return;
		
		for (final Player player : _coinQueue.keySet())
		{
			String tempCaller = null;
			int tempTotal = 0;
			
			for (String curCaller : _coinQueue.get(player).keySet())
			{
				tempCaller = curCaller;
				tempTotal += _coinQueue.get(player).get(curCaller);
			}
			
			final int total = tempTotal;
			final String caller = tempCaller;
			
			if (caller == null)
				continue;

			if (player.isOnline() && player.isValid())
				RewardCoins(null, caller, player.getName(), ClientManager.Get(player).getAccountId(), total, false);
			else
			{
				Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
				{
					public void run()
					{
						RewardCoins(null, caller, player.getName(), ClientManager.getCachedClientAccountId(player.getUniqueId()), total, false);
					}
				});
			}
			
			System.out.println("Queue Added [" + player + "] with Coins [" + total + "] for [" + caller + "]");
			
			//Clean
			_coinQueue.get(player).clear();
		}
		
		//Clean
		_coinQueue.clear();
	}
	
	public void RewardGold(Callback<Boolean> callback, String caller, String name, int accountId, int amount)
	{
		RewardGold(callback, caller, name, accountId, amount, true);
	}

	public void RewardGold(final Callback<Boolean> callback, final String caller, final String name, final int accountId, final int amount, final boolean updateTotal)
	{
		_repository.rewardGold(new Callback<Boolean>()
		{
			public void run(Boolean success)
			{
				if (success)
				{
					if (updateTotal)
					{
						Donor donor = Get(name);				
						
						if (donor != null)
						{
							donor.addGold(amount);
						}
					}
				}
				else
				{
					System.out.println("REWARD GOLD FAILED...");
				}
				
				if (callback != null)
					callback.run(true);
			}
		}, caller, name, accountId, amount);
	}
	
	public void RewardGoldLater(final String caller, final Player player, final int amount)
	{
		if (!_goldQueue.containsKey(player))
			_goldQueue.put(player, new NautHashMap<String, Integer>());
		
		int totalAmount = amount;
		
		if (_goldQueue.get(player).containsKey(caller))
			totalAmount += _goldQueue.get(player).get(caller);
		
		_goldQueue.get(player).put(caller, totalAmount);
		
		//Do Temp Change
		Donor donor = Get(player.getName());				
		
		if (donor != null)
			donor.addGold(amount);
	}
	
	@EventHandler
	public void UpdateGoldQueue(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWER)
			return;
		
		for (Player player : _goldQueue.keySet())
		{
			String caller = null;
			int total = 0;
			
			for (String curCaller : _goldQueue.get(player).keySet())
			{
				caller = curCaller;
				total += _goldQueue.get(player).get(curCaller);
			}
			
			if (caller == null)
				continue;

			//Actually Add Gold
			RewardGold(null, caller, player.getName(), ClientManager.Get(player).getAccountId(), total, false);
			
			System.out.println("Queue Added [" + player + "] with Gold [" + total + "] for [" + caller + "]");
			
			//Clean
			_goldQueue.get(player).clear();
		}
		
		//Clean
		_goldQueue.clear();
	}

	public void applyKits(String playerName)
	{
		_repository.applyKits(playerName);
	}
	
	@Override
	public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
	{
		Get(playerName).addGold(_repository.retrieveDonorInfo(resultSet).getGold());
	}

	@Override
	protected Donor AddPlayer(String player)
	{
		return new Donor();
	}

	@Override
	public String getQuery(int accountId, String uuid, String name)
	{
		return "SELECT gold FROM accounts WHERE id = '" + accountId + "';";
	}
}
