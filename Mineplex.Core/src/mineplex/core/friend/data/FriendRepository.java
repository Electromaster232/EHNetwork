package mineplex.core.friend.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.common.util.NautHashMap;
import mineplex.core.database.DBPool;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.ResultSetCallable;
import mineplex.core.database.column.ColumnVarChar;
import mineplex.core.friend.FriendStatusType;
import mineplex.serverdata.Region;
import mineplex.serverdata.data.DataRepository;
import mineplex.serverdata.data.PlayerStatus;
import mineplex.serverdata.redis.RedisDataRepository;
import mineplex.serverdata.servers.ServerManager;

public class FriendRepository extends RepositoryBase
{
	private static String CREATE_FRIEND_TABLE = "CREATE TABLE IF NOT EXISTS accountFriend (id INT NOT NULL AUTO_INCREMENT, uuidSource VARCHAR(100), uuidTarget VARCHAR(100), status VARCHAR(100), PRIMARY KEY (id), UNIQUE INDEX uuidIndex (uuidSource, uuidTarget));";
	private static String RETRIEVE_MULTIPLE_FRIEND_RECORDS = "SELECT uuidSource, tA.Name, status, tA.lastLogin, now() FROM accountFriend INNER Join accounts AS fA ON fA.uuid = uuidSource INNER JOIN accounts AS tA ON tA.uuid = uuidTarget WHERE uuidSource IN ";
	private static String ADD_FRIEND_RECORD = "INSERT INTO accountFriend (uuidSource, uuidTarget, status, created) SELECT fA.uuid AS uuidSource, tA.uuid AS uuidTarget, ?, now() FROM accounts as fA LEFT JOIN accounts AS tA ON tA.name = ? WHERE fA.name = ?;";
	private static String UPDATE_MUTUAL_RECORD = "UPDATE accountFriend AS aF INNER JOIN accounts as fA ON aF.uuidSource = fA.uuid INNER JOIN accounts AS tA ON aF.uuidTarget = tA.uuid SET aF.status = ? WHERE tA.name = ? AND fA.name = ?;";
	private static String DELETE_FRIEND_RECORD = "DELETE aF FROM accountFriend AS aF INNER JOIN accounts as fA ON aF.uuidSource = fA.uuid INNER JOIN accounts AS tA ON aF.uuidTarget = tA.uuid WHERE fA.name = ? AND tA.name = ?;";
	
	// Repository holding active PlayerStatus data.
	private DataRepository<PlayerStatus> _repository;
	
	public FriendRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
		
		_repository = new RedisDataRepository<PlayerStatus>(ServerManager.getMasterConnection(), ServerManager.getSlaveConnection(),
																Region.currentRegion(), PlayerStatus.class, "playerStatus");
	}
	
	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_FRIEND_TABLE);
	}

	@Override
	protected void update()
	{
	}
	
	public boolean addFriend(final Player caller, String name)
	{
		int rowsAffected = executeUpdate(ADD_FRIEND_RECORD, new ColumnVarChar("status", 100, "Sent"), new ColumnVarChar("name", 100, name), new ColumnVarChar("name", 100, caller.getName()));
		
		if (rowsAffected > 0)
			return executeUpdate(ADD_FRIEND_RECORD, new ColumnVarChar("status", 100, "Pending"), new ColumnVarChar("name", 100, caller.getName()), new ColumnVarChar("uuid", 100, name)) > 0;
			
		return false;
	}
	
	public boolean updateFriend(String caller, String name, String status)
	{			
		return executeUpdate(UPDATE_MUTUAL_RECORD, new ColumnVarChar("status", 100, status), new ColumnVarChar("uuid", 100, name), new ColumnVarChar("name", 100, caller)) > 0;
	}
	
	public boolean removeFriend(String caller, String name)
	{
		int rowsAffected = executeUpdate(DELETE_FRIEND_RECORD, new ColumnVarChar("name", 100, name), new ColumnVarChar("name", 100, caller));
		
		if (rowsAffected > 0)
			return executeUpdate(DELETE_FRIEND_RECORD, new ColumnVarChar("name", 100, caller), new ColumnVarChar("uuid", 100, name)) > 0;
			
		return false;
	}
	
	public NautHashMap<String, FriendData> getFriendsForAll(Player...players)
	{
		final NautHashMap<String, FriendData> friends = new NautHashMap<String, FriendData>();
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(RETRIEVE_MULTIPLE_FRIEND_RECORDS + "(");
		
		for (Player player : players)
		{
			stringBuilder.append("'" + player.getUniqueId() + "', ");
		}
		
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
		stringBuilder.append(");");

		executeQuery(stringBuilder.toString(), new ResultSetCallable()		
		{
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				Set<FriendData> friendDatas = new HashSet<FriendData>();
				while (resultSet.next())
				{
					FriendStatus friend = new FriendStatus();
					
					String uuidSource = resultSet.getString(1);
					friend.Name = resultSet.getString(2);
					friend.Status = Enum.valueOf(FriendStatusType.class, resultSet.getString(3));					
					friend.LastSeenOnline = resultSet.getTimestamp(5).getTime() - resultSet.getTimestamp(4).getTime();
					
					if (!friends.containsKey(uuidSource))
						friends.put(uuidSource, new FriendData());
						
					friends.get(uuidSource).getFriends().add(friend);
					
					friendDatas.add(friends.get(uuidSource));
				}
				
				// Load the server status of friends for all sources.
				for(FriendData friendData : friendDatas)
				{
					loadFriendStatuses(friendData);
				}
			}
		});
		
		return friends;
	}
	
	public FriendData loadClientInformation(ResultSet resultSet) throws SQLException
	{
		FriendData friendData = new FriendData();
		
		while (resultSet.next())
		{
			FriendStatus friend = new FriendStatus();
			
			friend.Name = resultSet.getString(1);
			friend.Status = Enum.valueOf(FriendStatusType.class, resultSet.getString(2));
			friend.LastSeenOnline = resultSet.getTimestamp(4).getTime() - resultSet.getTimestamp(3).getTime();
			friend.ServerName = null;
			friend.Online = (friend.ServerName != null);
			friendData.getFriends().add(friend);
		}

		loadFriendStatuses(friendData);
		
		return friendData;
	}
	
	/**
	 * Load the server status information for a list of {@link FriendStatus}.
	 * @param friendData - the {@link FriendStatus} object friends server status' are to be updated
	 * @param statuses - the fetched {@link PlayerStatus} associated with all online {@code friends}.
	 */	
	public void loadFriendStatuses(FriendData friendData)
	{
		// Generate a set of all friend names
		Set<String> friendNames = new HashSet<String>();
		for(FriendStatus status : friendData.getFriends())
		{
			friendNames.add(status.Name);
		}
		
		// Load PlayerStatus' for friends
		Collection<PlayerStatus> statuses = _repository.getElements(friendNames);
		
		// Load player statuses into a mapping
		Map<String, PlayerStatus> playerStatuses = new HashMap<String, PlayerStatus>();
		for(PlayerStatus status : statuses)
		{
			playerStatuses.put(status.getName(), status);
		}
		
		// Load status information into friend data.
		for (FriendStatus friend : friendData.getFriends())
		{
			PlayerStatus status = playerStatuses.get(friend.Name);
			friend.Online = (status != null);
			friend.ServerName = (friend.Online) ? status.getServer() : null;
		}
	}
	
	/**
	 * @param playerName - the name of the player whose current server status is being fetched
	 * @return the {@link MinecraftServer} name that the player matching {@code playerName}
	 * is currently online on, if they are online, null otherwise.
	 */
	public String fetchPlayerServer(String playerName)
	{
		PlayerStatus status = _repository.getElement(playerName);
		
		return (status == null) ? null : status.getServer();
	}
}
