package ehnetwork.core.benefit;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.database.DBPool;
import ehnetwork.core.database.RepositoryBase;
import ehnetwork.core.database.column.ColumnInt;
import ehnetwork.core.database.column.ColumnVarChar;

public class BenefitManagerRepository extends RepositoryBase
{
	private static String CREATE_BENEFIT_TABLE = "CREATE TABLE IF NOT EXISTS rankBenefits (id INT NOT NULL AUTO_INCREMENT, accountId INT, benefit VARCHAR(100), PRIMARY KEY (id), FOREIGN KEY (accountId) REFERENCES accounts(id));";
	
	private static String INSERT_BENEFIT = "INSERT INTO rankBenefits (accountId, benefit) VALUES (?, ?);";
		
	public BenefitManagerRepository(JavaPlugin plugin)
	{
		super(plugin, DBPool.ACCOUNT);
	}

	@Override
	protected void initialize()
	{
		executeUpdate(CREATE_BENEFIT_TABLE);
	}

	@Override
	protected void update()
	{
	}
	
	public boolean addBenefit(int accountId, String benefit)
	{
		return executeUpdate(INSERT_BENEFIT, new ColumnInt("accountId", accountId), new ColumnVarChar("benefit", 100, benefit)) > 0;
	}

	public BenefitData retrievePlayerBenefitData(ResultSet resultSet) throws SQLException
	{
		BenefitData playerBenefit = new BenefitData();
		
		while (resultSet.next())
		{
			playerBenefit.Benefits.add(resultSet.getString(1));			
		}
		
		playerBenefit.Loaded = true;
		
		return playerBenefit;
	}
}
