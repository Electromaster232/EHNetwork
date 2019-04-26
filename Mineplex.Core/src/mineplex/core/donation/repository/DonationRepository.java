package mineplex.core.donation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.common.util.Callback;
import mineplex.core.database.DBPool;
import mineplex.core.database.DatabaseRunnable;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.column.ColumnInt;
import mineplex.core.database.column.ColumnVarChar;
import mineplex.core.donation.Donor;
import mineplex.core.donation.repository.token.GemRewardToken;
import mineplex.core.donation.repository.token.PurchaseToken;
import mineplex.core.donation.repository.token.UnknownPurchaseToken;
import mineplex.core.server.remotecall.AsyncJsonWebCall;
import mineplex.core.server.remotecall.JsonWebCall;
import mineplex.core.server.util.TransactionResponse;

public class DonationRepository extends RepositoryBase
{
	private static String CREATE_COIN_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS accountCoinTransactions (id INT NOT NULL AUTO_INCREMENT, accountId INT, reason VARCHAR(100), coins INT, PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id));";
	private static String CREATE_GEM_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS accountGemTransactions (id INT NOT NULL AUTO_INCREMENT, accountId INT, reason VARCHAR(100), gems INT, PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id));";
	private static String INSERT_COIN_TRANSACTION = "INSERT INTO accountCoinTransactions(accountId, reason, coins) VALUES(?, ?, ?);";
	private static String UPDATE_ACCOUNT_COINS = "UPDATE accounts SET coins = coins + ? WHERE id = ?;";
	private static String UPDATE_ACCOUNT_GOLD = "UPDATE accounts SET gold = gold + ? WHERE id = ?;";
	private static String UPDATE_NULL_ACCOUNT_GEMS_AND_COINS_ = "UPDATE accounts SET gems = ?, coins = ? WHERE id = ? AND gems IS NULL AND coins IS NULL;";

	private String _webAddress;
	
	public DonationRepository(JavaPlugin plugin, String webAddress)
	{
		super(plugin, DBPool.ACCOUNT);
		
		_webAddress = webAddress;
	}
	
	public void PurchaseKnownSalesPackage(final Callback<TransactionResponse> callback, String name, final String uuid, final int cost, final int salesPackageId) 
	{
		final PurchaseToken token = new PurchaseToken();
		token.AccountName = name;
		token.UsingCredits = false;
		token.SalesPackageId = salesPackageId;

		final Callback<TransactionResponse> extraCallback = new Callback<TransactionResponse>()
		{
			public void run(final TransactionResponse response)
			{
				Bukkit.getServer().getScheduler().runTask(Plugin, new Runnable()
				{
					@Override
					public void run()
					{
						callback.run(response);
					}
				});
			}
		};
		
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				new JsonWebCall(_webAddress + "PlayerAccount/PurchaseKnownSalesPackage").Execute(TransactionResponse.class, extraCallback, token);
			}
		}), "Error purchasing known sales package in DonationRepository : ");
	}
	
	public void PurchaseUnknownSalesPackage(final Callback<TransactionResponse> callback, final String name, final int accountId, final String packageName, final boolean coinPurchase, final int cost) 
	{
		final UnknownPurchaseToken token = new UnknownPurchaseToken();
		token.AccountName = name;
		token.SalesPackageName = packageName;
		token.CoinPurchase = coinPurchase;
		token.Cost = cost;
		token.Premium = false;

		final Callback<TransactionResponse> extraCallback = new Callback<TransactionResponse>()
		{
			public void run(final TransactionResponse response)
			{
				if (response == TransactionResponse.Success)
				{
					if (coinPurchase)
					{
						executeUpdate(UPDATE_ACCOUNT_COINS, new ColumnInt("coins", -cost), new ColumnInt("id", accountId));
						//executeUpdate(INSERT_COIN_TRANSACTION, new ColumnInt("id", accountId), new ColumnVarChar("reason", 100, "Purchased " + packageName), new ColumnInt("coins", -cost));
					}
				}
				
				Bukkit.getServer().getScheduler().runTask(Plugin, new Runnable()
				{
					@Override
					public void run()
					{
						callback.run(response);
					}
				});
			}
		};
		
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				new JsonWebCall(_webAddress + "PlayerAccount/PurchaseUnknownSalesPackage").Execute(TransactionResponse.class, extraCallback, token);
			}
		}), "Error purchasing unknown sales package in DonationRepository : ");
	}
	
	public void gemReward(final Callback<Boolean> callback, final String giver, String name, final String uuid, final int greenGems)
	{
		final GemRewardToken token = new GemRewardToken();
		token.Source = giver;
		token.Name = name;
		token.Amount = greenGems;
		
		final Callback<Boolean> extraCallback = new Callback<Boolean>()
		{
			public void run(final Boolean response)
			{			
				Bukkit.getServer().getScheduler().runTask(Plugin, new Runnable()
				{
					@Override
					public void run()
					{
						callback.run(response);
					}
				});
			}
		};
		
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				new JsonWebCall(_webAddress + "PlayerAccount/GemReward").Execute(Boolean.class, extraCallback, token);
			}
		}), "Error updating player gem amount in DonationRepository : ");
	}
	
	public void rewardCoins(final Callback<Boolean> callback, final String giver, String name, final int accountId, final int coins)
	{
		final GemRewardToken token = new GemRewardToken();
		token.Source = giver;
		token.Name = name;
		token.Amount = coins;
		
		final Callback<Boolean> extraCallback = new Callback<Boolean>()
		{
			public void run(final Boolean response)
			{
				if (response)
				{
					executeUpdate(UPDATE_ACCOUNT_COINS, new ColumnInt("coins", coins), new ColumnInt("id", accountId));
					//executeUpdate(INSERT_COIN_TRANSACTION, new ColumnInt("id", accountId), new ColumnVarChar("reason", 100, "Rewarded by " + giver), new ColumnInt("coins", coins));
				}
				
				Bukkit.getServer().getScheduler().runTask(Plugin, new Runnable()
				{
					@Override
					public void run()
					{
						callback.run(response);
					}
				});
			}
		};
		
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				new JsonWebCall(_webAddress + "PlayerAccount/CoinReward").Execute(Boolean.class, extraCallback, token);
			}
		}), "Error updating player coin amount in DonationRepository : ");
	}
	
	public void rewardGold(final Callback<Boolean> callback, final String giver, final String name, final int accountId, final int gold)
	{		
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				if (executeUpdate(UPDATE_ACCOUNT_GOLD, new ColumnInt("gold", gold), new ColumnInt("id", accountId)) < 1)
				{
					callback.run(false);
				}
				else
					callback.run(true);
			}
		}), "Error updating player gold amount in DonationRepository : ");
	}

	@Override
	protected void initialize()
	{
		executeUpdate(CREATE_COIN_TRANSACTION_TABLE);
		executeUpdate(CREATE_GEM_TRANSACTION_TABLE);
	}

	@Override
	protected void update()
	{
	}

	public void updateGemsAndCoins(final int accountId, final int gems, final int coins)
	{
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				executeUpdate(UPDATE_NULL_ACCOUNT_GEMS_AND_COINS_, new ColumnInt("gems", gems), new ColumnInt("coins", coins), new ColumnInt("id", accountId));
			}
		}), "Error updating player's null gems and coins DonationRepository : ");
	}

	public void applyKits(String playerName)
	{
		new AsyncJsonWebCall(_webAddress + "PlayerAccount/ApplyKits").Execute(playerName);
	}

	public Donor retrieveDonorInfo(ResultSet resultSet) throws SQLException
	{
		Donor donor = new Donor();
		
		while (resultSet.next())
		{
			donor.addGold(resultSet.getInt(1));
		}
		
		return donor;
	}
}