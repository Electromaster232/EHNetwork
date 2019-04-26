package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public class OldChest extends SalesPackageBase
{	
	public OldChest(SalesPackageManager manager)
	{
		super(manager, "1 Old Chest");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales item " + playerName + " 1 Item Old Chest", "Give 1 Old Chest.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
