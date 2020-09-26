package ehnetwork.game.skyclans.clans.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.ResultSetCallable;
import ehnetwork.core.database.column.ColumnBoolean;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnTimestamp;
import ehnetwork.core.database.column.ColumnVarChar;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanAllianceToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanEnemyToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanMemberToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanTerritoryToken;
import ehnetwork.game.skyclans.clans.repository.tokens.ClanToken;
import ehnetwork.game.skyclans.clans.ClanInfo;

public class ClanRepository extends RepositoryBase
{
	private static String CREATE_CLAN_TABLE = "CREATE TABLE IF NOT EXISTS clans (id INT NOT NULL AUTO_INCREMENT, serverName VARCHAR(100), name VARCHAR(100), description VARCHAR(140), home VARCHAR(140), admin BIT(1), dateCreated DATETIME, lastOnline DATETIME, energy INT, PRIMARY KEY (id), INDEX clanName (name));";
	private static String CREATE_ACCOUNT_CLAN_TABLE = "CREATE TABLE IF NOT EXISTS accountClan (id INT NOT NULL AUTO_INCREMENT, accountId INT, clanId INT, clanRole VARCHAR(140), PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id), FOREIGN KEY (clanId) REFERENCES clans(id), INDEX clanIdIndex (clanId));";
	private static String CREATE_CLAN_TERRITORY_TABLE = "CREATE TABLE IF NOT EXISTS clanTerritory (id INT NOT NULL AUTO_INCREMENT, clanId INT, serverName VARCHAR(100), chunk VARCHAR(100), safe BIT(1), PRIMARY KEY (id), FOREIGN KEY (clanId) REFERENCES clans(id), INDEX clanIdIndex (clanId, serverName));";
	private static String CREATE_CLAN_ALLIANCE_TABLE = "CREATE TABLE IF NOT EXISTS clanAlliances (id INT NOT NULL AUTO_INCREMENT, clanId INT, otherClanId INT, trusted BIT(1), PRIMARY KEY (id), FOREIGN KEY (otherClanId) REFERENCES clans(id), FOREIGN KEY (clanId) REFERENCES clans(id), INDEX clanIdIndex (clanId));";

	private static String RETRIEVE_START_CLAN_INFO = "SELECT c.id, c.name, c.description, c.home, c.admin, c.energy, c.dateCreated, c.lastOnline, ct.chunk, ct.safe FROM clans AS c LEFT JOIN clanTerritory AS ct ON ct.clanId = c.id WHERE c.serverName = ?;";
	private static String RETRIEVE_CLAN_MEMBER_INFO = "SELECT c.name, a.name, a.uuid, clanRole FROM accountClan AS ac INNER JOIN accounts AS a ON a.id = ac.accountId INNER JOIN clans AS c on c.id = ac.clanId WHERE c.serverName = ?;";
	private static String RETRIEVE_CLAN_ALLIANCE_INFO = "SELECT c.name, cOther.name, ca.trusted FROM clanAlliances AS ca INNER JOIN clans AS c ON c.id = ca.clanId INNER JOIN clans as cOther ON cOther.id = ca.otherClanId WHERE c.serverName = ?;";
	private static String RETRIEVE_CLAN_ENEMY_INFO = "SELECT c.name, cOther.name, clanScore, otherClanScore, clanKills, otherClanKills, timeFormed FROM clanEnemies AS ce INNER JOIN clans AS c ON c.id = ce.clanId INNER JOIN clans as cOther ON cOther.id = ce.otherClanId WHERE c.serverName = ?;";
	
	private static String DELETE_CLAN_MEMBER = "DELETE aC FROM accountClan AS aC INNER JOIN accounts ON accounts.id = aC.accountId WHERE aC.clanId = ? AND accounts.name = ?;";
	private static String DELETE_CLAN_MEMBERS = "DELETE FROM accountClan WHERE clanId = ?;";	
	private static String DELETE_CLAN_TERRITORY = "DELETE FROM clanTerritory WHERE clanId = ? AND serverName = ? AND chunk = ?;";
	private static String DELETE_CLAN_TERRITORIES = "DELETE FROM clanTerritory WHERE clanId = ?;";
	private static String DELETE_CLAN_ALLIANCE = "DELETE FROM clanAlliances WHERE clanId = ? AND otherClanId = ?;";
	private static String DELETE_CLAN_ALLIANCES = "DELETE FROM clanAlliances WHERE clanId = ? OR otherClanId = ?;";
	private static String DELETE_CLAN_ENEMY = "DELETE FROM clanEnemies WHERE clanId = ? OR otherClanId = ?;";
	private static String DELETE_CLAN = "DELETE FROM clans WHERE id = ?;";
	
	private static String ADD_CLAN = "INSERT INTO clans (serverName, name, description, home, admin, dateCreated, energy, lastOnline) VALUES (?, ?, ?, ?, ?, now(), ?, now());";
	private static String ADD_CLAN_MEMBER = "INSERT INTO accountClan (accountId, clanId, clanRole) SELECT accounts.id, ?, ? FROM accounts WHERE accounts.name = ?;";
	private static String ADD_CLAN_ALLIANCE = "INSERT INTO clanAlliances (clanId, otherClanId, trusted) VALUES (?, ?, ?);";
	private static String ADD_CLAN_ENEMY = "INSERT INTO clanEnemies (clanId, otherClanId, timeFormed) VALUES (?, ?, now());";
	private static String ADD_CLAN_TERRITORY = "INSERT INTO clanTerritory (clanId, serverName, chunk, safe) VALUES (?, ?, ?, ?);";
	
	private static String UPDATE_CLAN = "UPDATE clans SET name = ?, description = ?, home = ?, admin = ?, energy = ?, lastOnline = ? WHERE id = ?;";
	private static String UPDATE_CLAN_MEMBER = "UPDATE accountClan AS AC INNER JOIN accounts ON accounts.id = AC.accountId SET AC.clanRole = ? WHERE AC.clanId = ? AND accounts.name = ?;";
	private static String UPDATE_CLAN_ALLIANCE = "UPDATE clanAlliances SET trusted = ? WHERE clanId = ? AND otherClanId = ?;";
	private static String UPDATE_CLAN_ENEMY = "UPDATE clanEnemies SET clanScore = ?, otherClanScore = ?, clanKills = ?, otherClanKills = ? WHERE clanId = ? AND otherClanId = ?;";
	private static String UPDATE_CLAN_TERRITORY = "UPDATE clanTerritory SET safe = ? WHERE serverName = ? AND chunk = ?;";
	
	private String _serverName;
	
	public ClanRepository(JavaPlugin plugin, String serverName)
	{
		super(plugin, DBPool.ACCOUNT);
		
		_serverName = serverName;
	}

	@Override
	protected void initialize()
	{
		executeUpdate(CREATE_CLAN_TABLE);
		executeUpdate(CREATE_ACCOUNT_CLAN_TABLE);
		executeUpdate(CREATE_CLAN_TERRITORY_TABLE);
		executeUpdate(CREATE_CLAN_ALLIANCE_TABLE);
	}

	public Collection<ClanToken> retrieveClans()
	{
		final NautHashMap<String, ClanToken> clans = new NautHashMap<String, ClanToken>();
		
		executeQuery(RETRIEVE_START_CLAN_INFO, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					ClanToken token = new ClanToken();
					token.Id = resultSet.getInt(1);
					token.Name = resultSet.getString(2);
					token.Description = resultSet.getString(3);
					token.Home = resultSet.getString(4);
					token.Admin = resultSet.getBoolean(5);
					token.Energy = resultSet.getInt(6);
					token.DateCreated = resultSet.getTimestamp(7);
					token.LastOnline = resultSet.getTimestamp(8);
					
					ClanTerritoryToken territoryToken = new ClanTerritoryToken();
					territoryToken.ClanName = token.Name;
					territoryToken.Chunk = resultSet.getString(9);
					territoryToken.Safe = resultSet.getBoolean(10);
					
					if (!clans.containsKey(token.Name))
					{
						clans.put(token.Name, token);
					}
					
					if (territoryToken.Chunk != null)
						clans.get(token.Name).Territories.add(territoryToken);
				}
			}
			
		}, new ColumnVarChar("serverName", 100, _serverName));
		
		executeQuery(RETRIEVE_CLAN_MEMBER_INFO, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					String clanName = resultSet.getString(1);
					
					if (clans.containsKey(clanName))
					{
						ClanMemberToken memberToken = new ClanMemberToken();
						memberToken.Name = resultSet.getString(2);
						memberToken.PlayerUUID = UUID.fromString(resultSet.getString(3));
						memberToken.ClanRole = resultSet.getString(4);
						
						clans.get(clanName).Members.add(memberToken);
					}
				}
			}
			
		}, new ColumnVarChar("serverName", 100, _serverName));
		
		executeQuery(RETRIEVE_CLAN_ALLIANCE_INFO, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					String clanName = resultSet.getString(1);
					
					if (clans.containsKey(clanName))
					{
						ClanAllianceToken allianceToken = new ClanAllianceToken();
						allianceToken.ClanName = resultSet.getString(2);
						allianceToken.Trusted = resultSet.getBoolean(3);
						
						clans.get(clanName).Alliances.add(allianceToken);
					}
				}
			}
			
		}, new ColumnVarChar("serverName", 100, _serverName));

		executeQuery(RETRIEVE_CLAN_ENEMY_INFO, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					ClanEnemyToken clanToken = new ClanEnemyToken();
					ClanEnemyToken otherClanToken = new ClanEnemyToken();

					//c.name, cOther.name, clanScore, otherClanScore, clanKills, otherClanKills
					String clanName = resultSet.getString(1);
					String otherClanName = resultSet.getString(2);
					int clanScore = resultSet.getInt(3);
					int otherClanScore = resultSet.getInt(4);
					int clanKills = resultSet.getInt(5);
					int otherClanKills = resultSet.getInt(6);
					Timestamp timeFormed = resultSet.getTimestamp(7);

					clanToken.EnemyName = otherClanName;
					clanToken.Score = clanScore;
					clanToken.Kills = clanKills;
					clanToken.Initiator = true;
					clanToken.TimeFormed = timeFormed;

					otherClanToken.EnemyName = clanName;
					otherClanToken.Score = otherClanScore;
					otherClanToken.Kills = otherClanKills;
					otherClanToken.Initiator = false;
					otherClanToken.TimeFormed = timeFormed;

					clans.get(clanName).EnemyToken = clanToken;
					clans.get(otherClanName).EnemyToken = otherClanToken;
				}
			}

		}, new ColumnVarChar("serverName", 100, _serverName));
		
		return clans.values();
	}
	
	@Override
	protected void update()
	{
	}

	public void deleteClan(int clanId)
	{
		executeUpdate(DELETE_CLAN, new ColumnInt("clanid", clanId));
	}

	public void addClan(final ClanInfo clan, final ClanToken token)
	{
		executeInsert(ADD_CLAN, new ResultSetCallable()
		{
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					clan.setId(resultSet.getInt(1));
				}
			}
			
		}, new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("name", 100, token.Name), new ColumnVarChar("description", 100, token.Description), new ColumnVarChar("home", 100, token.Home), new ColumnBoolean("admin", token.Admin), new ColumnInt("energy", token.Energy));		
		
	}

	public void addMember(int clanId, String playerName, String role)
	{
		executeUpdate(ADD_CLAN_MEMBER, new ColumnInt("clanid", clanId), new ColumnVarChar("clanRole", 100, role), new ColumnVarChar("name", 100, playerName));
	}

	public void removeMember(int clanId, String playerName)
	{
		executeUpdate(DELETE_CLAN_MEMBER, new ColumnInt("clanid", clanId), new ColumnVarChar("name", 100, playerName));
	}

	public void updateMember(int clanId, String playerName, String role)
	{
		executeUpdate(UPDATE_CLAN_MEMBER, new ColumnVarChar("clanRole", 100, role) , new ColumnInt("clanid", clanId), new ColumnVarChar("name", 100, playerName));
	}

	public void addClanRelationship(int clanId, int otherClanId, boolean trusted)
	{
		executeUpdate(ADD_CLAN_ALLIANCE, new ColumnInt("clanid", clanId), new ColumnInt("otherClanId", otherClanId), new ColumnBoolean("trusted", trusted));
	}

	public void updateClanRelationship(int clanId, int otherClanId, boolean trusted)
	{
		executeUpdate(UPDATE_CLAN_ALLIANCE, new ColumnBoolean("trusted", trusted), new ColumnInt("clanid", clanId), new ColumnInt("otherClanId", otherClanId));
	}

	public void removeClanRelationship(int clanId, int otherClanId)
	{
		executeUpdate(DELETE_CLAN_ALLIANCE, new ColumnInt("clanid", clanId), new ColumnInt("otherClanId", otherClanId));
	}

	public void addTerritoryClaim(int clanId, String chunk, boolean safe)
	{
		executeUpdate(ADD_CLAN_TERRITORY, new ColumnInt("clanId", clanId), new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("chunk", 100, chunk), new ColumnBoolean("safe", safe));
	}

	public void addEnemy(int clanId, int otherClanId)
	{
		executeUpdate(ADD_CLAN_ENEMY, new ColumnInt("clanId", clanId), new ColumnInt("otherClanId", otherClanId));
	}

	public void removeTerritoryClaim(int clanId, String chunk)
	{
		executeUpdate(DELETE_CLAN_TERRITORY, new ColumnInt("clanId", clanId), new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("chunk", 100, chunk));
	}

	public void updateClan(int clanId, String name, String desc, String home, boolean admin, int energy, Timestamp lastOnline)
	{
		executeUpdate(UPDATE_CLAN, new ColumnVarChar("name", 100, name), new ColumnVarChar("desc", 100, desc), new ColumnVarChar("home", 100, home), new ColumnBoolean("admin", admin), new ColumnInt("energy", energy), new ColumnTimestamp("lastOnline", lastOnline), new ColumnInt("clanId", clanId));
	}

	public void updateEnemy(int clanId, int otherClanId, int clanScore, int otherClanScore, int clanKills, int otherClanKills)
	{
		executeUpdate(UPDATE_CLAN_ENEMY, new ColumnInt("clanId", clanId), new ColumnInt("otherClanId", otherClanId),
				new ColumnInt("clanScore", clanScore), new ColumnInt("otherClanScore", otherClanScore), new ColumnInt("clanKills", clanKills),
				new ColumnInt("otherClanKills", otherClanKills), new ColumnInt("clanId", clanId), new ColumnInt("otherClanId", otherClanId));
	}

	public void updateTerritoryClaim(String chunk, boolean safe)
	{
		executeUpdate(UPDATE_CLAN_TERRITORY, new ColumnBoolean("safe", safe), new ColumnVarChar("serverName", 100, _serverName), new ColumnVarChar("chunk", 100, chunk));
	}
}
