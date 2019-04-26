package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public class MonthlyUltra extends SalesPackageBase
{
	public MonthlyUltra(SalesPackageManager manager)
	{
		super(manager, "Monthly Ultra");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales coin " + playerName + " 7500", " 7,500 Coins");
		addButton(agent, "/sales booster " + playerName  + " 30", " 30 Gem Boosters");
		addButton(agent, "/sales rank " + playerName + " ULTRA false", " Monthly Ultra.");
		addButton(agent, "Apply All", "/sales ultra " + playerName, " Apply all above.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
