package mineplex.hub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mineplex.core.common.util.NautHashMap;
import mineplex.core.database.DBPool;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HubRepository
{
	private boolean _us = true;
	
	private static String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS newsList (id INT NOT NULL AUTO_INCREMENT, newsString VARCHAR(256), newsPosition INT, PRIMARY KEY (id));";
	private static String RETRIEVE_NEWS_ENTRIES = "SELECT newsString, newsPosition FROM newsList;";
	private static String RETRIEVE_MAX_NEWS_POSITION = "SELECT MAX(newsPosition) AS newsPosition FROM newsList;";
	private static String ADD_NEWS_ENTRY = "INSERT INTO newsList (newsString, newsPosition) VALUES(?,?);";
	//private static String ADD_NEWS_ENTRY = "SET @max = (SELECT MAX(newsPosition) AS newsPosition FROM newsList);INSERT INTO newsList (newsString, newsPosition) VALUES(?,@max + 1);";
	private static String SET_NEWS_ENTRY = "UPDATE newsList SET newsString = ? WHERE newsPosition = ?;";
	private static String DELETE_NEWS_ENTRY = "DELETE FROM newsList WHERE newsPosition = ?;";
	private static String RECALC_NEWS_POSITIONS = "UPDATE newsList SET newsPosition = newsPosition - 1 WHERE newsPosition > ?;";
	//private static String DELETE_RECALC_NEWS_ENTRY = "SET @pos = ?;SET @max = (SELECT MAX(newsPosition) AS newsPosition FROM newsList);DELETE FROM newsList WHERE newsPosition = @pos;UPDATE newsList SET newsPosition = IF(@max <> @pos, newsPosition - 1, newsPosition) WHERE newsPosition > @pos;";
	
	public void initialize(boolean us) 
	{
		_us = us;
	}
	
	public HashMap<String, String> retrieveNewsEntries()
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		HashMap<String, String> newsEntries = new HashMap<String, String>();
		
		try (Connection connection = DBPool.MINEPLEX.getConnection())
		{
			preparedStatement = connection.prepareStatement(RETRIEVE_NEWS_ENTRIES);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				newsEntries.put(resultSet.getString(2), resultSet.getString(1));
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
		
		return newsEntries;
	}
	
	public boolean setNewsEntry(String newsEntry, int newsPosition)
	{
		int result = 0;
		PreparedStatement preparedStatement = null;

		try (Connection connection = DBPool.MINEPLEX.getConnection())
		{
			preparedStatement = connection.prepareStatement(SET_NEWS_ENTRY);
			preparedStatement.setString(1, newsEntry);
			preparedStatement.setInt(2, newsPosition);

			result = preparedStatement.executeUpdate();
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
		
		return result != 0;
	}
	
	public int retrieveMaxNewsPosition()
	{
		int result = 0;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;

		try (Connection connection = DBPool.MINEPLEX.getConnection())
		{
			preparedStatement = connection.prepareStatement(RETRIEVE_MAX_NEWS_POSITION);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				result = Integer.parseInt(resultSet.getString(1));
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
		
		return result;
	}
	
	public boolean addNewsEntry(String newsEntry)
	{
		int result = 0;
		int maxPos = retrieveMaxNewsPosition();
		PreparedStatement preparedStatement = null;

		try (Connection connection = DBPool.MINEPLEX.getConnection())
		{
			preparedStatement = connection.prepareStatement(ADD_NEWS_ENTRY);
			preparedStatement.setString(1, newsEntry);
			preparedStatement.setInt(2, maxPos + 1);

			result = preparedStatement.executeUpdate();
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
		
		return result != 0;
	}
	
	public boolean deleteNewsEntry(int newsPosition)
	{
		int result = 0;
		int maxPos = retrieveMaxNewsPosition();
		PreparedStatement preparedStatement = null;

		try (Connection connection = DBPool.MINEPLEX.getConnection())
		{
			//preparedStatement = connection.prepareStatement(DELETE_RECALC_NEWS_ENTRY);
			preparedStatement = connection.prepareStatement(DELETE_NEWS_ENTRY);
			preparedStatement.setInt(1, newsPosition);
			result = preparedStatement.executeUpdate();

			if (result != 0 && maxPos != newsPosition)
			{
				preparedStatement.close();

				preparedStatement = connection.prepareStatement(RECALC_NEWS_POSITIONS);
				preparedStatement.setInt(1, newsPosition);

				result = preparedStatement.executeUpdate();
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
		
		return result != 0;
	}
}
