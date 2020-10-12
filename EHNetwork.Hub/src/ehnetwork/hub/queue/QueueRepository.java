package ehnetwork.hub.queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ehnetwork.core.database.DBPool;

public class QueueRepository
{
	private boolean _us = true;
	
	private static String CREATE_ELO_QUEUE_TABLE = "CREATE TABLE IF NOT EXISTS playerQueue (id INT NOT NULL AUTO_INCREMENT, playerList VARCHAR(256), gameType VARCHAR(256), playerCount INT,  elo INT, state VARCHAR(256), time LONG, assignedMatch INT, US BOOLEAN NOT NULL DEFAULT '1', PRIMARY KEY (id), UNIQUE INDEX name_gametype (playerList, gameType));";
	private static String SAVE_STATE_VALUE = "UPDATE playerQueue SET state = ? WHERE id = ?;";
	private static String DELETE_QUEUE_RECORD = "DELETE FROM playerQueue WHERE id = ?;";
	private static String INSERT_ACCOUNT = "INSERT INTO playerQueue (playerList, gameType, elo, state, time, playerCount, assignedMatch) VALUES (?, ?, ?, 'Awaiting Match', now(), ?, -1) ON DUPLICATE KEY UPDATE time=VALUES(time);";
	private static String RETRIEVE_MATCH_STATUS = "SELECT state, assignedMatch FROM playerQueue WHERE id = ?;";
	private static String RETRIEVE_OTHER_MATCH_STATUS = "SELECT state, playerCount FROM playerQueue WHERE assignedMatch = ? AND id != ? ORDER BY id DESC;";
	
	public QueueRepository(boolean us)
	{
		_us = us;
		
		initialize();
	}
	
	public void initialize()
	{
		PreparedStatement preparedStatement = null;
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			// Create table
			preparedStatement = connection.prepareStatement(CREATE_ELO_QUEUE_TABLE);
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
	}

	public void deleteQueueRecord(PlayerMatchStatus matchStatus)
	{
		PreparedStatement preparedStatement = null;
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			preparedStatement = connection.prepareStatement(DELETE_QUEUE_RECORD);

			preparedStatement.setInt(1, matchStatus.Id);

			if (preparedStatement.executeUpdate() == 0)
			{
				System.out.println("Error deleting queue record.");
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
	
	public void updateState(PlayerMatchStatus matchStatus)
	{
		PreparedStatement preparedStatement = null;
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			preparedStatement = connection.prepareStatement(SAVE_STATE_VALUE);
			preparedStatement.setString(1, matchStatus.State);
			preparedStatement.setInt(2, matchStatus.Id);

			if (preparedStatement.executeUpdate() == 0)
			{
				System.out.println("Error updating state.");
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

	public PlayerMatchStatus addQueueRecord(String playerList, int playerCount, String gameType, int elo)
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		PlayerMatchStatus matchStatus = new PlayerMatchStatus();
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			preparedStatement = connection.prepareStatement(INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, playerList);
			preparedStatement.setString(2, gameType);
			preparedStatement.setInt(3, elo);
			//preparedStatement.setBoolean(4, _us);
			preparedStatement.setInt(4, playerCount);

			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			while (resultSet.next())
			{
				matchStatus.Id = resultSet.getInt(1);
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
		
		return matchStatus;
	}
	
	public PlayerMatchStatus updateQueueStatus(int id)
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		PlayerMatchStatus matchStatus = null;
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			preparedStatement = connection.prepareStatement(RETRIEVE_MATCH_STATUS);
			preparedStatement.setInt(1, id);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				matchStatus = new PlayerMatchStatus();

				matchStatus.Id = id;
				matchStatus.State = resultSet.getString(1);
				matchStatus.AssignedMatch = resultSet.getInt(2);
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
		
		return matchStatus;
	}
	
	public PlayerMatchStatus updateOtherPlayersMatchStatus(PlayerMatchStatus matchStatus)
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try (Connection connection = DBPool.QUEUE.getConnection())
		{
			preparedStatement = connection.prepareStatement(RETRIEVE_OTHER_MATCH_STATUS);
			preparedStatement.setInt(1, matchStatus.AssignedMatch);
			preparedStatement.setInt(2, matchStatus.Id);

			resultSet = preparedStatement.executeQuery();
			matchStatus.OtherStatuses.clear();

			while (resultSet.next())
			{
				int playerCount = resultSet.getInt(2);

				for (int i = 0; i < playerCount; i++)
					matchStatus.OtherStatuses.add(resultSet.getString(1));
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
		
		return matchStatus;
	}
}
