package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class LifetimeHero extends SalesPackageBase
{
	public LifetimeHero(SalesPackageManager manager)
	{
		super(manager, "Lifetime Hero");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales coin " + playerName + " 30000", " 30,000 Coins");
		addButton(agent, "/sales rank " + playerName + " HERO true", " Lifetime Hero.");
		addButton(agent, "Apply All", "/sales lifetimehero " + playerName, " Apply all above.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
