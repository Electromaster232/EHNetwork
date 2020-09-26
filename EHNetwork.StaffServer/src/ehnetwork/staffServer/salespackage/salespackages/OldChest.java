package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

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
