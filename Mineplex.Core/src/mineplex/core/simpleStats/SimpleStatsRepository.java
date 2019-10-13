package mineplex.core.simpleStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mineplex.core.common.util.NautHashMap;

public class SimpleStatsRepository 
{
	private static Object _connectionLock = new Object();
	
	private String _connectionString = "jdbc:mysql://192.168.1.139:3306/Mineplex?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
	private String _userName = "mineplex";
	private String _password = "r@P#eiXSQP(R"; //try to obfuscate this in the future!

	private static String CREATE_STATS_TABLE = "CREATE TABLE IF NOT EXISTS simpleStats (id INT NOT NULL AUTO_INCREMENT, statName VARCHAR(64), statValue VARCHAR(64), PRIMARY KEY (id));";
	private static String RETRIEVE_STATS_RECORDS = "SELECT simpleStats.statName, simpleStats.statValue FROM simpleStats;";	
	private static String STORE_STATS_RECORD = "INSERT INTO simpleStats (statName,statValue) VALUES(?,?);";
	private static String RETRIEVE_STAT_RECORD = "SELECT simpleStats.statName, simpleStats.statValue FROM simpleStats WHERE statName = '?';";
	
	private Connection _connection = null;
	
	public void initialize()
	{
		/*
		PreparedStatement preparedStatement = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			// Create table
			preparedStatement = _connection.prepareStatement(CREATE_STATS_TABLE);
			preparedStatement.execute();
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
	
	public NautHashMap<String, String> retrieveStatRecords()
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		NautHashMap<String, String> statRecords = new NautHashMap<String, String>();
		
		try
		{
			synchronized (_connectionLock)
			{
				if (_connection.isClosed())
				{
					_connection = DriverManager.getConnection(_connectionString, _userName, _password);
				}
				
				preparedStatement = _connection.prepareStatement(RETRIEVE_STATS_RECORDS);
				
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next())
				{
					statRecords.put(resultSet.getString(1), resultSet.getString(2));
				}
			}
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
			
			if (resultSet != null)
			{
				try
				{
					resultSet.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return statRecords;
	}
	
	public void storeStatValue(String statName, String statValue)
	{
		PreparedStatement preparedStatement = null;
		
		try
		{
			synchronized (_connectionLock)
			{
				if (_connection.isClosed())
				{
					_connection = DriverManager.getConnection(_connectionString, _userName, _password);
				}
								
				preparedStatement = _connection.prepareStatement(STORE_STATS_RECORD);
				preparedStatement.setString(1, statName);
				preparedStatement.setString(2, statValue);
				
				preparedStatement.executeUpdate();
			}
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
	
	public NautHashMap<String, String> retrieveStat(String statName)
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		NautHashMap<String, String> statRecords = new NautHashMap<String, String>();
		
		try
		{
			synchronized (_connectionLock)
			{
				if (_connection.isClosed())
				{
					_connection = DriverManager.getConnection(_connectionString, _userName, _password);
				}
				
				preparedStatement = _connection.prepareStatement(RETRIEVE_STAT_RECORD);
				preparedStatement.setString(1, statName);
				
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next())
				{
					statRecords.put(resultSet.getString(1), resultSet.getString(2));
				}
			}
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
			
			if (resultSet != null)
			{
				try
				{
					resultSet.close();
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return statRecords;
	}
}
