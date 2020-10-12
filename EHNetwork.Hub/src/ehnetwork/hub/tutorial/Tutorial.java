package ehnetwork.hub.tutorial;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.hub.HubManager;

public class Tutorial extends MiniPlugin 
{
	protected String _name;
	protected int _gems;
	protected String _task;
	
	protected ArrayList<TutorialPhase> _phases = new ArrayList<TutorialPhase>();
	protected HashMap<Player, TutorialData> _tute = new HashMap<Player, TutorialData>();

	protected String _main = ChatColor.RESET + "";
	protected String _elem = C.cYellow + C.Bold;

	public Tutorial(HubManager manager, String name, int gems, String task)
	{
		super(task, manager.getPlugin());
		
		_name = name;
		_gems = gems;
		_task = task;
	}
	
	public String GetTutName()
	{
		return _name;
	}
	
	public int GetGems()
	{
		return _gems;
	}
	
	public String GetTask()
	{
		return _task;
	}

	public void BeginTutorial(Player player)
	{
		_tute.put(player, new TutorialData(player, _phases.get(0)));
	}

	public boolean InTutorial(Player player)
	{
		return _tute.containsKey(player);
	}

	public void EndTutorial(Player player) 
	{
		_tute.remove(player);
	}
	
	public HashMap<Player, TutorialData> GetTutorial()
	{
		return _tute;
	}
	
	public ArrayList<TutorialPhase> GetPhases()
	{
		return _phases;
	}
}
