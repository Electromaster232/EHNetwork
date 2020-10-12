package ehnetwork.core.elo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnVarChar;

public class EloRepository extends RepositoryBase
{
	private static String CREATE_ELO_TABLE = "CREATE TABLE IF NOT EXISTS eloRating (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(256), gameType VARCHAR(256), elo INT, PRIMARY KEY (id), UNIQUE INDEX uuid_gameType_index (uuid, gameType));";
	private static String INSERT_ELO = "INSERT INTO eloRating (uuid, gameType, elo) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE elo=VALUES(elo);";

	public EloRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);

		initialize();
	}

	public void initialize()
	{
		//executeUpdate(CREATE_ELO_TABLE);
	}

	public void saveElo(String uuid, String gameType, int elo)
	{
		executeUpdate(INSERT_ELO, new ColumnVarChar("uuid", 100, uuid), new ColumnVarChar("gameType", 100, gameType),  new ColumnInt("elo", elo));
	}

	public EloClientData loadClientInformation(ResultSet resultSet) throws SQLException
	{
		EloClientData clientData = new EloClientData();

		while (resultSet.next())
		{
			clientData.Elos.put(resultSet.getString(1), resultSet.getInt(2));
		}

		return clientData;
	}

	@Override
	protected void update()
	{
	}
}
