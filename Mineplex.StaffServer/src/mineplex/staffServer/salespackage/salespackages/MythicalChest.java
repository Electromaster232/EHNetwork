package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

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
