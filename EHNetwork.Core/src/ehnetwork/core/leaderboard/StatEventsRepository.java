package ehnetwork.core.leaderboard;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnVarChar;
 
  
/**
 * StatEventsRepository offers the ability to insert and log newly generated stat events.
 * 
 * Intended for the purpose of statistical tracking of players.
 * @author MrTwiggy
 *
 */
public class StatEventsRepository extends RepositoryBase
{    

	// Insert or update stat events query
	/*private static String INSERT_EVENT = 
			"INSERT INTO statEvents(accountId, gamemode, serverGroup, type, value, date) "
			+ "VALUES (?, ?, ?, ?, ?, CURRENT_DATE()) "
			+ "ON DUPLICATE KEY UPDATE value=value+";*/
	
	private static String INSERT_EVENT =
			"INSERT INTO statEvents(accountId, gamemode, serverGroup, type, value, date) "
			+ "SELECT accounts.id, ?, ?, ?, ?, CURRENT_DATE() "
			  + "FROM accounts WHERE name = ? "
			+ "ON DUPLICATE KEY UPDATE value=value+";
	
	/**
	 * Class constructor
	 * @param plugin - the plugin responsible for instantiating this repository.
	 */
	public StatEventsRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}

	@Override
	protected void initialize()
	{
		//executeUpdate(CREATE_EVENTS_TABLE);
		//executeUpdate(CREATE_STAT_RELATION_TABLE);
	}

	@Override
	protected void update()
	{
		
	}
	
	/**
	 * Insert (or update) a new stat event record for today into the repository.
	 * @param accountId - the id of the account responsible for the stat event.
	 * @param gamemode - the id of the gamemode type at the time of the stat event.
	 * @param serverGroup - the server group id associated with the stat event.
	 * @param type - the type of stat event to be inserted (id of type).
	 * @param value - the integer based value denoting the actual statistic being logged.
	 */
	public void insertStatEvent(String playerName, int gamemode, String serverGroup, int type, int value)
	{
		// Hacky string concatanation - Don't judge me!!
		// TODO: How to handle outside value block parameters
		executeUpdate(INSERT_EVENT + value + ";", new ColumnInt("gamemode", gamemode), new ColumnVarChar("serverGroup", 100, serverGroup), 
				new ColumnInt("type", type), new ColumnInt("value", value), new ColumnVarChar("name", 100, playerName));
	}
}
