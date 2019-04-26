package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

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
