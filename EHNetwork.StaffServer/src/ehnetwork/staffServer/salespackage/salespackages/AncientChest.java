package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class AncientChest extends SalesPackageBase
{	
	public AncientChest(SalesPackageManager manager)
	{
		super(manager, "1 Ancient Chest");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales item " + playerName + " 1 Item Ancient Chest", "Give 1 Ancient Chest.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
