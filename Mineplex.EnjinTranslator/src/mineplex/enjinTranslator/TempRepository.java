package mineplex.enjinTranslator;

import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.database.DBPool;
import mineplex.core.database.RepositoryBase;
import mineplex.core.database.column.ColumnInt;
import mineplex.core.database.column.ColumnVarChar;

public class TempRepository extends RepositoryBase
{
	private static String INSERT_CLIENT_INVENTORY = "INSERT INTO accountInventory (accountId, itemId, count) SELECT accounts.id, 5, ? FROM accounts WHERE accounts.name = ? ON DUPLICATE KEY UPDATE count=count + VALUES(count);";
	
	public TempRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}
	
	public void addGemBooster(String name, int amount)
	{
		executeUpdate(INSERT_CLIENT_INVENTORY,  new ColumnInt("count", amount), new ColumnVarChar("name", 100, name));
	}

	@Override
	protected void initialize()
	{
	}

	@Override
	protected void update()
	{
	}
}
