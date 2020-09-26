package ehnetwork.staffServer.password;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.ResultSetCallable;
import ehnetwork.core.database.column.ColumnVarChar;

public class PasswordRepository extends RepositoryBase
{
	private static String CREATE_SERVER_PASSWORD_TABLE = "CREATE TABLE IF NOT EXISTS serverPassword (id INT NOT NULL AUTO_INCREMENT, server VARCHAR(100), password VARCHAR(100), PRIMARY KEY (id));";
	private static String RETRIEVE_SERVER_PASSWORD = "SELECT password FROM serverPassword WHERE server = ?;";
	private static String CREATE_SERVER_PASSWORD = "INSERT INTO serverPassword (server, password) VALUES(?, ?);";
	private static String UPDATE_SERVER_PASSWORD = "UPDATE serverPassword SET password = ? WHERE server = ?;";
	private static String REMOVE_SERVER_PASSWORD = "DELETE FROM serverPassword WHERE server = ?;";
	
	private String _serverName;
	
	public PasswordRepository(JavaPlugin plugin, String serverName)
	{
		super(plugin, DBPool.ACCOUNT);
		_serverName = serverName;
	}

	@Override
	protected void initialize()
	{
		executeUpdate(CREATE_SERVER_PASSWORD_TABLE);
	}

	@Override
	protected void update()
	{
	}

	public String retrievePassword()
	{
		final List<String> passwords = new ArrayList<String>();
		
		executeQuery(RETRIEVE_SERVER_PASSWORD, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					passwords.add(resultSet.getString(1));
				}
			}
	
		}, new ColumnVarChar("serverName", 100, _serverName));
		
		return passwords.size() > 0 ? passwords.get(0) : null;
	}
	
	public void updatePassword(String password)
	{
		executeUpdate(UPDATE_SERVER_PASSWORD, new ColumnVarChar("password", 100, password), new ColumnVarChar("serverName", 100, _serverName));
	}
	
	public void removePassword()
	{
		executeUpdate(REMOVE_SERVER_PASSWORD, new ColumnVarChar("serverName", 100, _serverName));
	}

	public void createPassword(String password)
	{
		executeUpdate(CREATE_SERVER_PASSWORD, new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("password", 100, password));
	}
}
