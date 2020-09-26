package ehnetwork.core.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ehnetwork.core.account.ILoginProcessor;
import ehnetwork.core.database.DBPool;
import ehnetwork.database.Tables;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.SQLDialect;
import org.jooq.TransactionalRunnable;
import org.jooq.Update;
import org.jooq.impl.DSL;

public class SecondaryStatHandler implements ILoginProcessor
{
	private StatsManager _statsManager;
	private StatsRepository _repository;
	
	public SecondaryStatHandler(StatsManager statsManager, StatsRepository repository)
	{
		_statsManager = statsManager;
		_repository = repository;
	}

	@Override
	public String getName()
	{
		return "Secondary Stat Handler";
	}

	@Override
	public void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException
	{
		PlayerStats oldPlayerStats = _statsManager.Get(playerName);
		PlayerStats newPlayerStats = _repository.loadClientInformation(resultSet);
		
		if (newPlayerStats.getStatsNames().size() == 0 && oldPlayerStats.getStatsNames().size() != 0)
		{
			try
			{
				DSLContext context = DSL.using(DBPool.ACCOUNT, SQLDialect.MYSQL);

				final List<Insert> inserts = new ArrayList<>();

				for (String statName : oldPlayerStats.getStatsNames())
				{
					Integer statId = _statsManager.getStatId(statName);

					Insert insert = context
							.insertInto(Tables.accountStat)
							.set(Tables.accountStat.accountId, accountId)
							.set(Tables.accountStat.statId, statId)
							.set(Tables.accountStat.value, Math.max(oldPlayerStats.getStat(statName), 0L));

					inserts.add(insert);
				}
				
				context.transaction(new TransactionalRunnable()
				{
					@Override
					public void run(Configuration config) throws Exception
					{
						DSL.using(config).batch(inserts).execute();
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			_statsManager.replacePlayerHack(playerName, newPlayerStats);
			
			if (oldPlayerStats.getStatsNames().size() != 0)
			{
				try
				{
					final DSLContext context = DSL.using(DBPool.ACCOUNT, SQLDialect.MYSQL);
					final List<Update> updates = new ArrayList<>();
					final List<Insert> inserts = new ArrayList<>();
					boolean foundNegativeStat = false;
					boolean foundLessThanStat = false;
					
					for (String statName : oldPlayerStats.getStatsNames())
					{
						Integer statId = _statsManager.getStatId(statName);
						
						Insert insert = context
								.insertInto(Tables.accountStat)
								.set(Tables.accountStat.accountId, accountId)
								.set(Tables.accountStat.statId, statId)
								.set(Tables.accountStat.value, Math.max(oldPlayerStats.getStat(statName), 0L));

						inserts.add(insert);
						
						Update update = context
								.update(Tables.accountStat)
								.set(Tables.accountStat.value, Math.max(oldPlayerStats.getStat(statName), 0L))
								.where(Tables.accountStat.accountId.eq(accountId))
								.and(Tables.accountStat.statId.eq(statId));
	
						updates.add(update);
						
						if (oldPlayerStats.getStat(statName) < 0)
						{
							foundNegativeStat = true;
						}
						else if (newPlayerStats.getStat(statName) < oldPlayerStats.getStat(statName))
						{
							foundLessThanStat = true;
						}
					}
					
					if (foundNegativeStat && foundLessThanStat)
					{
						context.transaction(new TransactionalRunnable()
						{
							@Override
							public void run(Configuration config) throws Exception
							{
								int[] updateResult = context.batch(updates).execute();

								for (int i = 0; i < updateResult.length; i++)
								{
									if (updateResult[i] > 0)
										inserts.set(i, null);
								}

								inserts.removeAll(Collections.singleton(null));

								context.batch(inserts).execute();

								System.out.println("Updating");
							}
						});
					}
					
/*
					final List<Delete> deletes = new ArrayList<>();

					for (String statName : oldPlayerStats.getStatsNames())
					{
						Delete delete = context.delete(Tables.accountStats)
									.where(Tables.accountStats.accountId.equal(accountId));
						
						deletes.add(delete);
					}
					
					context.transaction(new TransactionalRunnable()
					{
						@Override
						public void run(Configuration config) throws Exception
						{
							DSL.using(config).batch(deletes).execute();
						}
					});
					*/
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getQuery(int accountId, String uuid, String name)
	{
		return "SELECT stats.name, value FROM accountStat INNER JOIN stats ON stats.id = accountStat.statId WHERE accountStat.accountId = '" + accountId + "';";
	}

}
