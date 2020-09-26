package ehnetwork.core.inventory.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.ResultSetCallable;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnVarChar;
import ehnetwork.core.inventory.ClientInventory;
import ehnetwork.core.inventory.ClientItem;

public class InventoryRepository extends RepositoryBase
{
	private static String CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS items (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100), categoryId INT, rarity INT, PRIMARY KEY (id), FOREIGN KEY (categoryId) REFERENCES itemCategories(id), UNIQUE INDEX uniqueNameCategoryIndex (name, categoryId));";
	private static String CREATE_INVENTORY_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS itemCategories (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100), PRIMARY KEY (id), UNIQUE INDEX nameIndex (name));";
	private static String CREATE_INVENTORY_RELATION_TABLE = "CREATE TABLE IF NOT EXISTS accountInventory (id INT NOT NULL AUTO_INCREMENT, accountId INT NOT NULL, itemId INT NOT NULL, count INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id), FOREIGN KEY (itemId) REFERENCES items(id), UNIQUE INDEX accountItemIndex (accountId, itemId));";
	
	private static String INSERT_ITEM = "INSERT INTO items (name, categoryId) VALUES (?, ?);";
	private static String RETRIEVE_ITEMS = "SELECT items.id, items.name, itemCategories.name FROM items INNER JOIN itemCategories ON itemCategories.id = items.categoryId;";
	
	private static String INSERT_CATEGORY = "INSERT INTO itemCategories (name) VALUES (?);";
	private static String RETRIEVE_CATEGORIES = "SELECT id, name FROM itemCategories;";
	
	private static String INSERT_CLIENT_INVENTORY = "INSERT INTO accountInventory (accountId, itemId, count) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE count=count + VALUES(count);";
	private static String UPDATE_CLIENT_INVENTORY = "UPDATE accountInventory SET count = count + ? WHERE accountId = ? AND itemId = ?;";

	public InventoryRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}

	@Override
	protected void initialize()
	{
		/*
		executeUpdate(CREATE_INVENTORY_CATEGORY_TABLE);
		executeUpdate(CREATE_INVENTORY_TABLE);
		executeUpdate(CREATE_INVENTORY_RELATION_TABLE);
		*/
	}

	@Override
	protected void update()
	{
	}

	public List<Category> retrieveCategories()
	{
		final List<Category> categories = new ArrayList<Category>();
		
		executeQuery(RETRIEVE_CATEGORIES, new ResultSetCallable()
		{
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					categories.add(new Category(resultSet.getInt(1), resultSet.getString(2)));
				}
			}
		});
		
		return categories;
	}
	
	public void addItem(String name, int categoryId)
	{
		executeUpdate(INSERT_ITEM, new ColumnVarChar("name", 100, name), new ColumnInt("categoryId", categoryId));
	}
	
	public void addCategory(String name)
	{
		executeUpdate(INSERT_CATEGORY, new ColumnVarChar("name", 100, name));
	}
	
	public List<Item> retrieveItems()
	{
		final List<Item> items = new ArrayList<Item>();
		
		executeQuery(RETRIEVE_ITEMS, new ResultSetCallable()
		{
			public void processResultSet(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					items.add(new Item(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
				}
			}
		});
		
		return items;
	}
	
	public boolean incrementClientInventoryItem(int accountId, int itemId, int count)
	{
		//System.out.println("Updating " + accountId + "'s " + itemId + " with " + count);
		if (executeUpdate(UPDATE_CLIENT_INVENTORY, new ColumnInt("count", count), new ColumnInt("id", accountId), new ColumnInt("itemid", itemId)) < 1)
		{
			//System.out.println("Inserting " + accountId + "'s " + itemId + " with " + count);
			return executeUpdate(INSERT_CLIENT_INVENTORY, new ColumnInt("id", accountId), new ColumnInt("itemid", itemId), new ColumnInt("count", count)) > 0;
		}
		else
			return true;
	}
	
	public ClientInventory loadClientInformation(ResultSet resultSet) throws SQLException
	{
		final ClientInventory clientInventory = new ClientInventory();

		while (resultSet.next())
		{
			clientInventory.addItem(new ClientItem(new Item(resultSet.getString(1), resultSet.getString(2)), resultSet.getInt(3)));
		}
		
		return clientInventory;
	}
}
