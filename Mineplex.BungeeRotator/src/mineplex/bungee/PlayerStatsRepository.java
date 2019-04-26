package mineplex.bungee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatsRepository
{
	private Connection _connection = null;
	private String _connectionString = "jdbc:mysql://149.56.38.19:3306/PlayerStats?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&allowMultiQueries=true";
	private String _userName = "djelectr_minep";
	private String _password = "r@P#eiXSQP(R";
	
	private static String SELECT_IPINFO = "SELECT id, ipAddress FROM ipInfo WHERE regionName IS NULL LIMIT 1000;";
	private static String UPDATE_IPINFO = "UPDATE ipInfo SET countryCode = ?, countryName = ?, regionCode = ?, regionName = ?, city = ?, zipCode = ?, timeZone = ?, latitude = ?, longitude = ?, metroCode = ? WHERE id = ?;";
	
	public void initialize()
	{
		try
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		System.out.println("Initialized PlayerStats.");
	}
	
	public List<IpInfo> getIpAddresses()
	{
		List<IpInfo> ipinfos = new ArrayList<IpInfo>(1000);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try 
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(SELECT_IPINFO);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next())
			{
				IpInfo ipInfo = new IpInfo();
				ipInfo.id = resultSet.getInt(1);
				ipInfo.ipAddress = resultSet.getString(2);
				
				ipinfos.add(ipInfo);
			}
			
			resultSet.close();
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
		
		return ipinfos;
	}
	
	public void updateIps(List<IpInfo> ips)
	{
		PreparedStatement preparedStatement = null;
		
		try 
		{
			if (_connection == null || _connection.isClosed())
				_connection = DriverManager.getConnection(_connectionString, _userName, _password);
			
			preparedStatement = _connection.prepareStatement(UPDATE_IPINFO);
			
			for (IpInfo ipInfo : ips)
			{
				preparedStatement.setString(1, ipInfo.countryCode);
				preparedStatement.setString(2, ipInfo.countryName);
				preparedStatement.setString(3, ipInfo.regionCode);
				preparedStatement.setString(4, ipInfo.regionName);
				preparedStatement.setString(5, ipInfo.city);
				preparedStatement.setString(6, ipInfo.zipCode);
				preparedStatement.setString(7, ipInfo.timeZone);
				preparedStatement.setDouble(8, ipInfo.latitude);
				preparedStatement.setDouble(9, ipInfo.longitude);
				preparedStatement.setInt(10, ipInfo.metroCode);
				preparedStatement.setInt(11, ipInfo.id);
				
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
}
