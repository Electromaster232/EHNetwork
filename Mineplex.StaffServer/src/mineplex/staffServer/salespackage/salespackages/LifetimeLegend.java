package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public class LifetimeLegend extends SalesPackageBase
{
	public LifetimeLegend(SalesPackageManager manager)
	{
		super(manager, "Lifetime Legend");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales coin " + playerName + " 60000", " 60,000 Coins");
		addButton(agent, "/sales rank " + playerName + " LEGEND true", " Lifetime Legend.");
		addButton(agent, "/sales item " + playerName + " 1 Morph Wither Morph", "Gives Wither Morph.");
		addButton(agent, "/sales item " + playerName + " 1 Pet Widder", "Gives Wither Pet.");
		addButton(agent, "/sales item " + playerName + " 1 Particle Legendary Aura", "Gives Legendary Aura Particle.");
		addButton(agent, "Apply All", "/sales lifetimelegend " + playerName, " Apply all above.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
