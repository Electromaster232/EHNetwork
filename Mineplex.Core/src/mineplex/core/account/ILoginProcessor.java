package mineplex.core.account;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ILoginProcessor
{
	String getName();
	
	void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException;

	String getQuery(int accountId, String uuid, String name);
}
