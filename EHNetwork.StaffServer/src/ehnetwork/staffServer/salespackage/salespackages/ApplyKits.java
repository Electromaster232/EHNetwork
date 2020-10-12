package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class ApplyKits extends SalesPackageBase
{	
	public ApplyKits(SalesPackageManager manager)
	{
		super(manager, "Apply Kits");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales kits " + playerName, "Apply Kit Unlocks.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
