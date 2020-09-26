package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class EasterBunny extends SalesPackageBase
{	
	public EasterBunny(SalesPackageManager manager)
	{
		super(manager, "Easter Bunny Morph");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales item " + playerName + " 1 Morph Easter Bunny Morph", "Give Easter Bunny Morph.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
