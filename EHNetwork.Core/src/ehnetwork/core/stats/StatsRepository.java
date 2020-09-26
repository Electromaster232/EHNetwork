package ehnetwork.core.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.ResultSetCallable;
import ehnetwork.core.database.column.ColumnVarChar;
import ehnetwork.database.Tables;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Update;
import org.jooq.impl.DSL;

public class StatsRepository extends RepositoryBase
{
	private static String CREATE_STAT_TABLE = "CREATE TABLE IF NOT EXISTS stats (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100), PRIMARY KEY (id), UNIQUE INDEX nameIndex (name));";
	private static String CREATE_STAT_RELATION_TABLE = "CREATE TABLE IF NOT EXISTS accountStats (id INT NOT NULL AUTO_INCREMENT, accountId INT NOT NULL, statId INT NOT NULL, value INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id), FOREIGN KEY (statId) REFERENCES stats(id), UNIQUE INDEX accountStatIndex (accountId, statId));";
	
	private static String RETRIEVE_STATS = "SELECT id, name FROM stats;";
	private static String INSERT_STAT = "INSERT INTO stats (name) VALUES (?);";	
	
	public StatsRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}

	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_STAT_TABLE);
		//executeUpdate(CREATE_STAT_RELATION_TABLE);
	}

	@Override
	protected void update()
	{
	}
	
	public List<Stat> retrieveStats()
	{
		final List<Stat> stats = new ArrayList<Stat>();
		
		executeQuery(RETRIEVE_STATS, new ResultSetCallable()
		{
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					Stat stat = new Stat();
					
					stat.Id = resultSet.getInt(1);
					stat.Name = resultSet.getString(2);

					stats.add(stat);
				}
			}
		});
		
		return stats;
	}
	
	public void addStat(String name)
	{
		executeUpdate(INSERT_STAT, new ColumnVarChar("name", 100, name));
	}

	@SuppressWarnings("rawtypes")
	public void saveStats(NautHashMap<Integer, NautHashMap<Integer, Long>> uploadQueue)
	{
		try
		{
			DSLContext context = DSL.using(getConnectionPool(), SQLDialect.MYSQL);

			List<Update> updates = new ArrayList<>();
			List<Insert> inserts = new ArrayList<>();

			for (int accountId : uploadQueue.keySet())
			{
				for (Integer statId : uploadQueue.get(accountId).keySet())
				{
					Update update = context
							.update(Tables.accountStat)
							.set(Tables.accountStat.value, Tables.accountStat.value.plus(uploadQueue.get(accountId).get(statId)))
							.where(Tables.accountStat.accountId.eq(accountId))
							.and(Tables.accountStat.statId.eq(statId));

					updates.add(update);

					Insert insert = context
							.insertInto(Tables.accountStat)
							.set(Tables.accountStat.accountId, accountId)
							.set(Tables.accountStat.statId, statId)
							.set(Tables.accountStat.value, uploadQueue.get(accountId).get(statId));

					inserts.add(insert);
				}
			}

			int[] updateResult = context.batch(updates).execute();

			for (int i = 0; i < updateResult.length; i++)
			{
				if (updateResult[i] > 0)
					inserts.set(i, null);
			}

			inserts.removeAll(Collections.singleton(null));

			context.batch(inserts).execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public PlayerStats loadOfflinePlayerStats(String playerName)
	{
		PlayerStats playerStats = null;

		DSLContext context;

		synchronized (this)
		{
			context = DSL.using(getConnectionPool(), SQLDialect.MYSQL);
		}

		Result<Record2<String, Long>> result = context.select(Tables.stats.name, Tables.accountStat.value).from(Tables.accountStat)
				.join(Tables.stats)
				.on(Tables.stats.id.eq(Tables.accountStat.statId))
				.where(Tables.accountStat.accountId.eq(DSL.select(Tables.accounts.id)
						.from(Tables.accounts)
						.where(Tables.accounts.name.eq(playerName)))
				).fetch();


		if (result.isNotEmpty())
		{
			playerStats = new PlayerStats();
			for (Record2<String, Long> record : result)
			{
				playerStats.addStat(record.value1(), record.value2());
			}
		}

		return playerStats;
	}

	public PlayerStats loadClientInformation(ResultSet resultSet) throws SQLException
	{
		final PlayerStats playerStats = new PlayerStats();
		
		while (resultSet.next())
		{
			playerStats.addStat(resultSet.getString(1), resultSet.getInt(2));
		}
		
		return playerStats;
	}
}
