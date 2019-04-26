package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

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
