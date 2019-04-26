package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public class Coins extends SalesPackageBase
{
	private int _amount;
	
	public Coins(SalesPackageManager manager, int amount)
	{
		super(manager, amount + " Coins");
		
		_amount = amount;
	}
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales coin " + playerName + " " + _amount, _amount + " Coins.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
