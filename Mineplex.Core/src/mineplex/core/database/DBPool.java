package mineplex.core.database;

import javax.sql.DataSource;
import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;

public final class DBPool
{
	public static final DataSource ACCOUNT = openDataSource("jdbc:mysql://38.108.20.53:3306/Account", "mineplex", "r@P#eiXSQP(R");
	public static final DataSource QUEUE = openDataSource("jdbc:mysql://38.108.20.53:3306/djelectro_Queue", "djelectro_minep", "r@P#eiXSQP(R");
	public static final DataSource MINEPLEX = openDataSource("jdbc:mysql://38.108.20.53:3306/Mineplex", "mineplex", "r@P#eiXSQP(R");
	public static final DataSource STATS_MINEPLEX = openDataSource("jdbc:mysql://38.108.20.53:3306/Mineplex", "mineplex", "r@P#eiXSQP(R");

	private static DataSource openDataSource(String url, String username, String password)
	{
		BasicDataSource source = new BasicDataSource();
		source.addConnectionProperty("autoReconnect", "true");
		source.addConnectionProperty("allowMultiQueries", "true");
		source.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl(url);
		source.setUsername(username);
		source.setPassword(password);
		source.setMaxTotal(4);
		source.setMaxIdle(4);
		source.setTimeBetweenEvictionRunsMillis(180 * 1000);
		source.setSoftMinEvictableIdleTimeMillis(180 * 1000);

		return source;
	}

	private DBPool()
	{

	}
}
