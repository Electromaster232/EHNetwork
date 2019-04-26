package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

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
