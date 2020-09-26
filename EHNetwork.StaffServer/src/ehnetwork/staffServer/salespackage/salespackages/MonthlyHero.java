package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class MonthlyHero extends SalesPackageBase
{
	public MonthlyHero(SalesPackageManager manager)
	{
		super(manager, "Monthly Hero");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{	
		addButton(agent, "/sales coin " + playerName + " 15000", " 15,000 Coins");
		addButton(agent, "/sales booster " + playerName  + " 90", " 90 Gem Boosters");
		addButton(agent, "/sales rank " + playerName + " HERO false", " Monthly Hero.");
		addButton(agent, "Apply All", "/sales hero " + playerName, " Apply all above.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
