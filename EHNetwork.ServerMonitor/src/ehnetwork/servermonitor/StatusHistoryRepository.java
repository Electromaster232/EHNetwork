package ehnetwork.servermonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import ehnetwork.serverdata.Region;
import ehnetwork.serverdata.data.DedicatedServer;
import ehnetwork.serverdata.data.ServerGroup;

public class StatusHistoryRepository
{
	private String _connectionString = "jdbc:mysql://192.168.1.139:3306/ServerStats";
	private String _userName = "mineplex";
	private String _password = "r@P#eiXSQP(R";
	
	private static String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS ServerGroupStats (id INT NOT NULL AUTO_INCREMENT, serverGroup VARCHAR(100), updated LONG, players INT, maxPlayers INT, totalNetworkCpuUsage DOUBLE(4,2), totalNetworkRamUsage DOUBLE(4,2), totalCpu MEDIUMINT, totalRam MEDIUMINT, US BOOLEAN NOT NULL DEFAULT '1', PRIMARY KEY (id));";
	private static String CREATE_DEDICATED_TABLE = "CREATE TABLE IF NOT EXISTS DedicatedServerStats (id INT NOT NULL AUTO_INCREMENT, serverName VARCHAR(100), address VARCHAR(25), updated LONG, cpu TINYINT, ram MEDIUMINT, usedCpuPercent DOUBLE(4,2), usedRamPercent DOUBLE(4,2), US BOOLEAN NOT NULL DEFAULT '1', PRIMARY KEY (id));";
	private static String CREATE_BUNGEE_TABLE = "CREATE TABLE IF NOT EXISTS BungeeStats (id INT NOT NULL AUTO_INCREMENT, address VARCHAR(25), updated LONG, players INT, maxPlayers INT, alive BOOLEAN NOT NULL, online BOOLEAN NOT NULL, US BOOLEAN NOT NULL DEFAULT '1', PRIMARY KEY (id));";
	private static String CREATE_NETWORKSTATS_TABLE = "CREATE TABLE IF NOT EXISTS NetworkStats (id INT NOT NULL AUTO_INCREMENT, updated LONG, players INT, totalNetworkCpuUsage DOUBLE(4,2), totalNetworkRamUsage DOUBLE(4,2), totalCpu MEDIUMINT, totalRam MEDIUMINT, US BOOLEAN NOT NULL DEFAULT '1', PRIMARY KEY (id));";

	private static String RETRIEVE_BUNGEE_STATUSES = "SELECT bungeeStatus.address, bungeeStatus.players, bungeeStatus.maxPlayers, bungeeStatus.US, onlineStatus.online, now(), bungeeStatus.updated FROM BungeeServers AS bungeeStatus INNER JOIN bungeeOnlineStatus AS onlineStatus ON onlineStatus.address = bungeeStatus.address";
	
	private static String INSERT_SERVERGROUP_STATS = "INSERT INTO ServerGroupStats (serverGroup, updated, players, maxPlayers, totalNetworkCpuUsage, totalNetworkRamUsage, totalCpu, totalRam, US) VALUES (?, now(), ?, ?, ?, ?, ?, ?, ?);";
	private static String INSERT_DEDICATEDSERVER_STATS = "INSERT INTO DedicatedServerStats (serverName, address, updated, cpu, ram, usedCpuPercent, usedRamPercent, US) VALUES (?, ?, now(), ?, ?, ?, ?, ?);";
	private static String INSERT_BUNGEE_STATS = "INSERT INTO BungeeStats (address, updated, players, maxPlayers, alive, online, US) VALUES (?, now(), ?, ?, ?, ?, ?);";
	private static String INSERT_NETWORK_STATS = "INSERT INTO NetworkStats (updated, players, totalNetworkCpuUsage, totalNetworkRamUsage, totalCpu, totalRam, US) VALUES (now(), ?, ?, ?, ?, ?, ?);";
	
	private static Connection _connection;
	private static Connection _bungeeconnection;
	
	public StatusHistoryRepository()
	{
		PreparedStatement preparedStatement = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			// Create table
			preparedStatement = _connection.prepareStatement(CREATE_GROUP_TABLE);
			preparedStatement.execute();
			preparedStatement.close();
			
			// Create table
			preparedStatement = _connection.prepareStatement(CREATE_DEDICATED_TABLE);
			preparedStatement.execute();
			preparedStatement.close();
			
			// Create table
			preparedStatement = _connection.prepareStatement(CREATE_BUNGEE_TABLE);
			preparedStatement.execute();
			preparedStatement.close();
			
			// Create table
			preparedStatement = _connection.prepareStatement(CREATE_NETWORKSTATS_TABLE);
			preparedStatement.execute();
			preparedStatement.close();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (preparedStatement != null)
			{
				try
				{
					preparedStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void saveServerGroupStats(int totalCpu, int totalRam, Collection<ServerGroup> collection)
	{
		PreparedStatement preparedStatement = null;
		
		try
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(INSERT_SERVERGROUP_STATS);
			
			for (ServerGroup serverGroup : collection)
			{
				int serverCpu = serverGroup.getServerCount() * serverGroup.getRequiredCpu();
				int serverRam = serverGroup.getServerCount() * serverGroup.getRequiredRam();
				
				preparedStatement.setString(1, serverGroup.getName());
				preparedStatement.setInt(2, serverGroup.getPlayerCount());
				preparedStatement.setInt(3, serverGroup.getMaxPlayerCount());
				preparedStatement.setDouble(4, (double)serverCpu / (double)totalCpu * 100d);
				preparedStatement.setDouble(5, (double)serverRam / (double)totalRam * 100d);
				preparedStatement.setInt(6, serverCpu);
				preparedStatement.setInt(7, serverRam);
				preparedStatement.setBoolean(8, serverGroup.getRegion() == Region.US);
				preparedStatement.addBatch();		
			}
			
			preparedStatement.executeBatch();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (preparedStatement != null)
			{
				try
				{
					preparedStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveDedicatedServerStats(List<DedicatedServer> dedicatedServers)
	{
		PreparedStatement preparedStatement = null;
		
		try
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(INSERT_DEDICATEDSERVER_STATS);
			
			for (DedicatedServer dedicatedServer : dedicatedServers)
			{
				double usedCpu = dedicatedServer.getMaxCpu() == 0 ? 0 : (1d - (double)dedicatedServer.getAvailableCpu() / (double)dedicatedServer.getMaxCpu()) * 100d;
				double usedRam = dedicatedServer.getMaxRam() == 0 ? 0 : (1d - (double)dedicatedServer.getAvailableRam() / (double)dedicatedServer.getMaxRam()) * 100d;
								
				preparedStatement.setString(1, dedicatedServer.getName());
				preparedStatement.setString(2, dedicatedServer.getPrivateAddress());
				preparedStatement.setInt(3, dedicatedServer.getMaxCpu());
				preparedStatement.setInt(4, dedicatedServer.getMaxRam());
				preparedStatement.setDouble(5, usedCpu);
				preparedStatement.setDouble(6, usedRam);
				preparedStatement.setBoolean(7, dedicatedServer.getRegion() == Region.US);
				preparedStatement.addBatch();		
			}
			
			preparedStatement.executeBatch();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (preparedStatement != null)
			{
				try
				{
					preparedStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void saveNetworkStats(double usedCpuPercent, double usedRamPercent, double availableCPU, double availableRAM, Region region)
	{
		/*
		int totalPlayers = 0;
		
		List<BungeeStatusData> bungeeStatuses = new ArrayList<BungeeStatusData>();
		PreparedStatement retrieveStatement = null;
		
		try
		{
			if (_bungeeconnection == null || _bungeeconnection.isClosed())
				_bungeeconnection = DriverManager.getConnection(_bungeeConnectionString, _userName, _password);
			
			retrieveStatement = _bungeeconnection.prepareStatement(RETRIEVE_BUNGEE_STATUSES);
			ResultSet resultSet = retrieveStatement.executeQuery();
			
			while (resultSet.next())
			{
				BungeeStatusData bungeeData = new BungeeStatusData();
				bungeeData.Address = resultSet.getString(1);
				bungeeData.Players = resultSet.getInt(2);
				bungeeData.MaxPlayers = resultSet.getInt(3);
				bungeeData.US = resultSet.getBoolean(4);
				bungeeData.Online = resultSet.getBoolean(5);
				
				long now = resultSet.getLong(6);
				long updated = resultSet.getLong(7);

				bungeeData.Alive = (now - updated) <= 15;
				
				if (bungeeData.Alive && bungeeData.US == (region == Region.US))
					totalPlayers += bungeeData.Players;
				
				if (bungeeData.US == (region == Region.US))
					bungeeStatuses.add(bungeeData);
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (retrieveStatement != null)
			{
				try
				{
					retrieveStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		PreparedStatement preparedStatement = null;
		
		try
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(INSERT_BUNGEE_STATS);
			
			for (BungeeStatusData bungeeStatusData : bungeeStatuses)
			{
					preparedStatement.setString(1, bungeeStatusData.Address);
					preparedStatement.setInt(2, bungeeStatusData.Players);
					preparedStatement.setInt(3, bungeeStatusData.MaxPlayers);
					preparedStatement.setBoolean(4, bungeeStatusData.Alive);
					preparedStatement.setBoolean(5, bungeeStatusData.Online);
					preparedStatement.setBoolean(6, bungeeStatusData.US);
					preparedStatement.addBatch();
			}
			
			preparedStatement.executeBatch();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (preparedStatement != null)
			{
				try
				{
					preparedStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		preparedStatement = null;
		
		try
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(INSERT_NETWORK_STATS);
			preparedStatement.setInt(1, totalPlayers);
			preparedStatement.setDouble(2, usedCpuPercent);
			preparedStatement.setDouble(3, usedRamPercent);
			preparedStatement.setInt(4, (int)availableCPU);
			preparedStatement.setInt(5, (int)availableRAM);
			preparedStatement.setBoolean(6, region == Region.US);
			
			preparedStatement.executeUpdate();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (preparedStatement != null)
			{
				try
				{
					preparedStatement.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		*/
	}
}
