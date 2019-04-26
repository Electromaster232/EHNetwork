package mineplex.core.preferences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.common.util.NautHashMap;
import mineplex.core.database.DBPool;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.column.ColumnVarChar;

public class PreferencesRepository extends RepositoryBase
{	
	//private static String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS accountPreferences (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(256), games BOOL NOT NULL DEFAULT 1, visibility BOOL NOT NULL DEFAULT 1, showChat BOOL NOT NULL DEFAULT 1, friendChat BOOL NOT NULL DEFAULT 1, privateMessaging BOOL NOT NULL DEFAULT 1, partyRequests BOOL NOT NULL DEFAULT 0, invisibility BOOL NOT NULL DEFAULT 0, forcefield BOOL NOT NULL DEFAULT 0, showMacReports BOOL NOT NULL DEFAULT 0, ignoreVelocity BOOL NOT NULL DEFAULT 0, PRIMARY KEY (id), UNIQUE INDEX uuid_index (uuid));";
	private static String INSERT_ACCOUNT = "INSERT INTO accountPreferences (uuid) VALUES (?) ON DUPLICATE KEY UPDATE uuid=uuid;";
	private static String UPDATE_ACCOUNT_PREFERENCES = "UPDATE accountPreferences SET games = ?, visibility = ?, showChat = ?, friendChat = ?, privateMessaging = ?, partyRequests = ?, invisibility = ?, forcefield = ?, showMacReports = ?, ignoreVelocity = ?, pendingFriendRequests = ?, friendDisplayInventoryUI = ? WHERE uuid=?;";

	public PreferencesRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}
	
	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_ACCOUNT_TABLE);
	}

	@Override
	protected void update()
	{
	}
	
	public void saveUserPreferences(NautHashMap<String, UserPreferences> preferences)
	{
		try 
			(
				Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_PREFERENCES);
			)
		{
			for (Entry<String, UserPreferences> entry : preferences.entrySet())
			{
				preparedStatement.setBoolean(1, entry.getValue().HubGames);
				preparedStatement.setBoolean(2, entry.getValue().ShowPlayers);
				preparedStatement.setBoolean(3, entry.getValue().ShowChat);
				preparedStatement.setBoolean(4, entry.getValue().FriendChat);
				preparedStatement.setBoolean(5, entry.getValue().PrivateMessaging);
				preparedStatement.setBoolean(6, entry.getValue().PartyRequests);
				preparedStatement.setBoolean(7, entry.getValue().Invisibility);
				preparedStatement.setBoolean(8, entry.getValue().HubForcefield);
				preparedStatement.setBoolean(9, entry.getValue().ShowMacReports);
				preparedStatement.setBoolean(10, entry.getValue().IgnoreVelocity);
                preparedStatement.setBoolean(11, entry.getValue().PendingFriendRequests);
                preparedStatement.setBoolean(12, entry.getValue().friendDisplayInventoryUI);
				preparedStatement.setString(13, entry.getKey());
				
				preparedStatement.addBatch();
			}

			int[] rowsAffected = preparedStatement.executeBatch();
			int i = 0;
			
			for (Entry<String, UserPreferences> entry : preferences.entrySet())
			{
				if (rowsAffected[i] < 1)
				{
					executeUpdate(INSERT_ACCOUNT, new ColumnVarChar("uuid", 100, entry.getKey()));
					
					preparedStatement.setBoolean(1, entry.getValue().HubGames);
					preparedStatement.setBoolean(2, entry.getValue().ShowPlayers);
					preparedStatement.setBoolean(3, entry.getValue().ShowChat);
					preparedStatement.setBoolean(4, entry.getValue().FriendChat);
					preparedStatement.setBoolean(5, entry.getValue().PrivateMessaging);
					preparedStatement.setBoolean(6, entry.getValue().PartyRequests);
					preparedStatement.setBoolean(7, entry.getValue().Invisibility);
					preparedStatement.setBoolean(8, entry.getValue().HubForcefield);
					preparedStatement.setBoolean(9, entry.getValue().ShowMacReports);
					preparedStatement.setBoolean(10, entry.getValue().IgnoreVelocity);
					preparedStatement.setBoolean(11, entry.getValue().PendingFriendRequests);
	                preparedStatement.setBoolean(12, entry.getValue().friendDisplayInventoryUI);
	                preparedStatement.setString(13, entry.getKey());
					preparedStatement.execute();
				}
				
				i++;
			}
		}		
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public UserPreferences loadClientInformation(final ResultSet resultSet) throws SQLException
	{
		final UserPreferences preferences = new UserPreferences();
		
		if (resultSet.next())
		{
			preferences.HubGames = resultSet.getBoolean(1);
			preferences.ShowPlayers = resultSet.getBoolean(2);
			preferences.ShowChat = resultSet.getBoolean(3);
			preferences.FriendChat = resultSet.getBoolean(4);
			preferences.PrivateMessaging = resultSet.getBoolean(5);
			preferences.PartyRequests = resultSet.getBoolean(6);
			preferences.Invisibility = resultSet.getBoolean(7);
			preferences.HubForcefield = resultSet.getBoolean(8);
			preferences.ShowMacReports = resultSet.getBoolean(9);
			preferences.IgnoreVelocity = resultSet.getBoolean(10);
			preferences.PendingFriendRequests = resultSet.getBoolean(11);
			preferences.friendDisplayInventoryUI = resultSet.getBoolean(12);
		}
		
		return preferences;
	}
}
