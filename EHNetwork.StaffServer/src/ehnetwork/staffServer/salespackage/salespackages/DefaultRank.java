package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class DefaultRank extends SalesPackageBase
{
	public DefaultRank(SalesPackageManager manager)
	{
		super(manager, "Default Rank");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales rank " + playerName + " ALL false", " Default Rank.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
