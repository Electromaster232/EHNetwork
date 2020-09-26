package ehnetwork.staffServer.salespackage.salespackages;

import org.bukkit.entity.Player;

import ehnetwork.staffServer.salespackage.SalesPackageManager;

public class MythicalChest extends SalesPackageBase
{	
	public MythicalChest(SalesPackageManager manager)
	{
		super(manager, "1 Mythical Chest");
	}
	
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales item " + playerName + " 1 Item Mythical Chest", "Give 1 Mythical Chest.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
