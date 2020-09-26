package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class LifetimeUltra extends SalesPackageBase
{
	public LifetimeUltra(SalesPackageManager manager)
	{
		super(manager, "Lifetime Ultra");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales coin " + playerName + " 15000", " 15,000 Coins");
		addButton(agent, "/sales rank " + playerName + " ULTRA true", " Lifetime Ultra.");
		addButton(agent, "Apply All", "/sales lifetimeultra " + playerName, " Apply all above.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
