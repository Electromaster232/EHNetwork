package mineplex.staffServer.salespackage.salespackages;

import mineplex.staffServer.salespackage.SalesPackageManager;

import org.bukkit.entity.Player;

public class GemHunter extends SalesPackageBase
{
	private int _level;
	
	public GemHunter(SalesPackageManager manager, int level)
	{
		super(manager, "Level " + level + " Gem Hunter");
		
		_level = level;
	}
	public void displayToAgent(Player agent, String playerName)
	{
		addButton(agent, "/sales gemhunter " + playerName + " " + _level, "Level " + _level + " Gem Hunter.");
		agent.sendMessage(" ");
		addBackButton(agent, playerName);
	}
}
