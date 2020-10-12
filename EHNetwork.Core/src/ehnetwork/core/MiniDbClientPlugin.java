package ehnetwork.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.ILoginProcessor;

public abstract class MiniDbClientPlugin<DataType extends Object> extends MiniClientPlugin<DataType> implements ILoginProcessor
{
	protected CoreClientManager ClientManager = null;
	
	public MiniDbClientPlugin(String moduleName, JavaPlugin plugin, CoreClientManager clientManager) 
	{
		super(moduleName, plugin);
		
		ClientManager = clientManager;
		
		clientManager.addStoredProcedureLoginProcessor(this);
	}

	public abstract void processLoginResultSet(String playerName, int accountId, ResultSet resultSet) throws SQLException;
	
	public CoreClientManager getClientManager()
	{
		return ClientManager;
	}
}
