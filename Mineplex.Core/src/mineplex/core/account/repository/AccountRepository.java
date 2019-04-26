package mineplex.core.account.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.account.ILoginProcessor;
import mineplex.core.account.repository.token.LoginToken;
import mineplex.core.account.repository.token.RankUpdateToken;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.database.DBPool;
import mineplex.core.database.DatabaseRunnable;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.ResultSetCallable;
import mineplex.core.database.column.ColumnBoolean;
import mineplex.core.database.column.ColumnTimestamp;
import mineplex.core.database.column.ColumnVarChar;
import mineplex.core.server.remotecall.JsonWebCall;

public class AccountRepository extends RepositoryBase
{	
	private static String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS accounts (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(100), name VARCHAR(40), gems INT, rank VARCHAR(40), rankPerm BOOL, rankExpire LONG, lastLogin LONG, totalPlayTime LONG, PRIMARY KEY (id), UNIQUE INDEX uuidIndex (uuid), UNIQUE INDEX nameIndex (name), INDEX rankIndex (rank));";
	private static String ACCOUNT_LOGIN_NEW =  "INSERT INTO accounts (uuid, name, lastLogin) values(?, ?, now());";
	private static String UPDATE_ACCOUNT_RANK = "UPDATE accounts SET rank=?, rankPerm=false, rankExpire=now() + INTERVAL 1 MONTH WHERE uuid = ?;";
	private static String UPDATE_ACCOUNT_RANK_DONOR = "UPDATE accounts SET rank=?, donorRank=?, rankPerm=false, rankExpire=now() + INTERVAL 1 MONTH WHERE uuid = ?;";
	private static String UPDATE_ACCOUNT_RANK_PERM = "UPDATE accounts SET rank=?, rankPerm=true WHERE uuid = ?;";
	private static String UPDATE_ACCOUNT_RANK_DONOR_PERM = "UPDATE accounts SET rank=?, donorRank=?, rankPerm=true WHERE uuid = ?;";
	private static String UPDATE_ACCOUNT_NULL_RANK = "UPDATE accounts SET rank=?, donorRank=?, rankPerm=?, rankExpire=? WHERE uuid = ? AND rank IS NULL;";
	
	private static String SELECT_ACCOUNT_UUID_BY_NAME = "SELECT uuid FROM accounts WHERE name = ? ORDER BY lastLogin DESC;";
	
	private String _webAddress;
	
	public AccountRepository(JavaPlugin plugin, String webAddress)
	{
		super(plugin, DBPool.ACCOUNT);
		
		_webAddress = webAddress;
	}

	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_ACCOUNT_TABLE);
	}
	
	public int login(NautHashMap<String, ILoginProcessor> loginProcessors, String uuid, String name)
	{
		int accountId = -1;
		try (
				Connection connection = getConnection();
				Statement statement = connection.createStatement()
			)
		{
			statement.execute("SELECT id FROM accounts WHERE accounts.uuid = '" + uuid + "' LIMIT 1;");
			ResultSet resultSet = statement.getResultSet();
			
			while (resultSet.next())
			{
				accountId = resultSet.getInt(1);
			}
			
			if (accountId == -1)
			{
				final List<Integer> tempList = new ArrayList<Integer>(1);
				
				executeInsert(ACCOUNT_LOGIN_NEW, new ResultSetCallable()
				{
					@Override
					public void processResultSet(ResultSet resultSet) throws SQLException
					{
						while (resultSet.next())
						{
							tempList.add(resultSet.getInt(1));
						}
					}
				},new ColumnVarChar("uuid", 100, uuid), new ColumnVarChar("name", 100, name));
				
				accountId = tempList.get(0);
			}
			
			/*
			boolean statementStatus = statement.execute(
					"UPDATE accounts SET name='" + name + "', lastLogin=now() WHERE accounts.uuid = '" + uuid + "';"
					+ "SELECT games, visibility, showChat, friendChat, privateMessaging, partyRequests, invisibility, forcefield, showMacReports, ignoreVelocity, pendingFriendRequests FROM accountPreferences WHERE accountPreferences.uuid = '" + uuid + "' LIMIT 1;"
					+ "SELECT items.name, ic.name as category, count FROM accountInventory AS ai INNER JOIN items ON items.id = ai.itemId INNER JOIN itemCategories AS ic ON ic.id = items.categoryId INNER JOIN accounts ON accounts.id = ai.accountId WHERE accounts.uuid = '" + uuid + "';"
					+ "SELECT benefit FROM rankBenefits WHERE rankBenefits.uuid = '" + uuid + "';"
					+ "SELECT stats.name, value FROM accountStats INNER JOIN stats ON stats.id = accountStats.statId INNER JOIN accounts ON accountStats.accountId = accounts.id WHERE accounts.uuid = '" + uuid + "';"
					+ "SELECT tA.Name, status, serverName, tA.lastLogin, now() FROM accountFriend INNER Join accounts AS fA ON fA.uuid = uuidSource INNER JOIN accounts AS tA ON tA.uuid = uuidTarget LEFT JOIN playerMap ON tA.name = playerName WHERE uuidSource = '" + uuid + "';"
					+ "SELECT gameType, elo FROM eloRating WHERE uuid = '" + uuid + "';"
					);
*/

			String loginString = "UPDATE accounts SET name='" + name + "', lastLogin=now() WHERE id = '" + accountId + "';";

			for (ILoginProcessor loginProcessor : loginProcessors.values())
			{
				loginString += loginProcessor.getQuery(accountId, uuid, name);
			}
			
			statement.execute(loginString);
			
			/*
			while (true)
			{
				if (statementStatus)
				{
					System.out.println("ResultSet : " + statement.getResultSet().getMetaData().getColumnCount() + " columns:");
					
					for (int i = 0; i < statement.getResultSet().getMetaData().getColumnCount(); i++)
					{
						System.out.println(statement.getResultSet().getMetaData().getColumnName(i + 1));
					}
				}
				else
				{
                    if (statement.getUpdateCount() == -1)
                        break;

					System.out.println("Update statement : " + statement.getUpdateCount() + " rows affected.");
				}
				
				statementStatus = statement.getMoreResults();
			}
			
			System.out.println("Done");
			*/
			
			statement.getUpdateCount();
			statement.getMoreResults();
			
			for (ILoginProcessor loginProcessor : loginProcessors.values())
			{
				loginProcessor.processLoginResultSet(name, accountId, statement.getResultSet());
				statement.getMoreResults();
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		return accountId;
	}
	
	public String GetClient(String name, UUID uuid, String ipAddress)
	{
		LoginToken token = new LoginToken();
		token.Name = name;
		token.Uuid = uuid.toString();
		token.IpAddress = ipAddress;

		return new JsonWebCall(_webAddress + "PlayerAccount/Login").ExecuteReturnStream(token);
	}
	
	public String getClientByUUID(UUID uuid)
	{
		return new JsonWebCall(_webAddress + "PlayerAccount/GetAccountByUUID").ExecuteReturnStream(uuid.toString());
	}

	public UUID getClientUUID(String name)
	{
		final List<UUID> uuids = new ArrayList<UUID>();
		
		executeQuery(SELECT_ACCOUNT_UUID_BY_NAME, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					uuids.add(UUID.fromString(resultSet.getString(1)));
				}
			}
		}, new ColumnVarChar("name", 100, name));
		
		if (uuids.size() > 0)
			return uuids.get(uuids.size() - 1);
		else
			return null;
	}
	
	public void saveRank(final Callback<Rank> callback, final String name, final UUID uuid, final Rank rank, final boolean perm)
	{
		final RankUpdateToken token = new RankUpdateToken();
		token.Name = name;
		token.Rank = rank.toString();
		token.Perm = perm;
		
		final Callback<Rank> extraCallback = new Callback<Rank>()
		{
			public void run(final Rank response)
			{
				if (rank == Rank.ULTRA || rank == Rank.HERO || rank == Rank.LEGEND)
				{
					if (perm)
						executeUpdate(UPDATE_ACCOUNT_RANK_DONOR_PERM, new ColumnVarChar("rank", 100, rank.toString()), new ColumnVarChar("donorRank", 100, rank.toString()), new ColumnVarChar("uuid", 100, uuid.toString()));
					else
						executeUpdate(UPDATE_ACCOUNT_RANK_DONOR, new ColumnVarChar("rank", 100, rank.toString()), new ColumnVarChar("donorRank", 100, rank.toString()), new ColumnVarChar("uuid", 100, uuid.toString()));
				}
				else
				{
					if (perm)
						executeUpdate(UPDATE_ACCOUNT_RANK_PERM, new ColumnVarChar("rank", 100, rank.toString()), new ColumnVarChar("uuid", 100, uuid.toString()));
					else
						executeUpdate(UPDATE_ACCOUNT_RANK, new ColumnVarChar("rank", 100, rank.toString()), new ColumnVarChar("uuid", 100, uuid.toString()));
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
				new JsonWebCall(_webAddress + "PlayerAccount/RankUpdate").Execute(Rank.class, extraCallback, token);
			}
		}), "Error saving player  " + token.Name + "'s rank in AccountRepository : ");
		
	}
	
	public void matchPlayerName(final Callback<List<String>> callback, final String userName)
	{
		Thread asyncThread = new Thread(new Runnable()
		{
			public void run()
			{
				List<String> tokenList = new JsonWebCall(_webAddress + "PlayerAccount/GetMatches").Execute(new TypeToken<List<String>>(){}.getType(), userName);
				callback.run(tokenList);
			}
		});
		
		asyncThread.start();
	}

	@Override
	protected void update()
	{
	}
	
	public void updateMysqlRank(final String uuid, final String rank, final boolean perm, final String rankExpire)
	{
		handleDatabaseCall(new DatabaseRunnable(new Runnable()
		{
			public void run()
			{
				executeUpdate(UPDATE_ACCOUNT_NULL_RANK, new ColumnVarChar("rank", 100, rank), new ColumnVarChar("donorRank", 100, rank), new ColumnBoolean("rankPerm", perm), new ColumnTimestamp("rankExpire", Timestamp.valueOf(rankExpire)), new ColumnVarChar("uuid", 100, uuid));
			}
		}), "Error updating player's mysql rank AccountRepository : ");
	}

	public String getClientByName(String playerName)
	{
		return new JsonWebCall(_webAddress + "PlayerAccount/GetAccount").ExecuteReturnStream(playerName);
	}
}
