package mineplex.staffServer.salespackage.salespackages;

import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public abstract class SalesPackageBase
{
	private String _name;
	
	protected SalesPackageManager Manager;
	
	protected SalesPackageBase(SalesPackageManager manager, String name)
	{
		Manager = manager;
		_name = name;
	}
	
	public abstract void displayToAgent(Player agent, String playerName);
	
	public String getName()
	{
		return _name;
	}
	
	protected void addButton(Player agent, String command, String itemText)
	{
		addButton(agent, "Apply", command, itemText);
	}
	
	protected void addButton(Player agent, String buttonText, String command, String itemText)
	{
		new JsonMessage("[").color("blue").extra(buttonText).color("green").click("run_command", command)
				.add("] ").color("blue").add(itemText).color("yellow").sendToPlayer(agent);
	}
	
	protected void addBackButton(Player agent, String playerName)
	{
		new JsonMessage("[").color("blue").extra("Back").color("green").click("run_command", "/display " + playerName + " ALL")
				.add("] ").color("blue").sendToPlayer(agent);
	}
}
