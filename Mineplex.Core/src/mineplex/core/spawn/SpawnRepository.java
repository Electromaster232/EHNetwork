package mineplex.core.spawn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.database.DBPool;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.ResultSetCallable;
import mineplex.core.database.column.ColumnVarChar;

public class SpawnRepository extends RepositoryBase
{
	private static String CREATE_SPAWN_TABLE = "CREATE TABLE IF NOT EXISTS spawns (id INT NOT NULL AUTO_INCREMENT, serverName VARCHAR(100), location VARCHAR(100), PRIMARY KEY (id), INDEX serverNameIndex (serverName));";
	private static String RETRIEVE_SPAWNS = "SELECT location FROM spawns WHERE serverName = ?;";
	private static String ADD_SERVER_SPAWN = "INSERT INTO spawns (serverName, location) VALUES (?, ?);";
	private static String DELETE_SERVER_SPAWN = "DELETE FROM spawns WHERE serverName = ?;";
	
	private String _serverName;
	
	public SpawnRepository(JavaPlugin plugin, String serverName)
	{
		super(plugin, DBPool.ACCOUNT);
		_serverName = serverName;
	}

	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_SPAWN_TABLE);
	}

	@Override
	protected void update()
	{
	}
	
	public void addSpawn(String location)
	{
		executeUpdate(ADD_SERVER_SPAWN, new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("location", 100, location));
	}
	
	public void clearSpawns()
	{
		executeUpdate(DELETE_SERVER_SPAWN, new ColumnVarChar("serverName", 100, _serverName));
	}
	
	public List<String> retrieveSpawns()
	{
		final List<String> spawns = new ArrayList<String>();
		
		executeQuery(RETRIEVE_SPAWNS, new ResultSetCallable()
		{
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					spawns.add(resultSet.getString(1));
				}
			}
		}, new ColumnVarChar("serverName", 100, _serverName));
		
		return spawns;
	}
}
