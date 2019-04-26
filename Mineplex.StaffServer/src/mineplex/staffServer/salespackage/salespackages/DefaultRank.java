package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

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
