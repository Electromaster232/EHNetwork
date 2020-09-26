package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class FrostLord extends SalesPackageBase
{	
	public FrostLord(SalesPackageManager manager)
	{
		super(manager, "Frost Lord");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales item " + playerName + " 1 Particle Frost Lord", "Give Frost Lord Particle.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
