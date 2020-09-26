package ehnetwork.core.scoreboard.elements;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import ehnetwork.core.scoreboard.ScoreboardManager;

public class ScoreboardElementText extends ScoreboardElement
{
	private String _line;
	
	public ScoreboardElementText(String line)
	{
		_line = line;
	}
	
	@Override
	public ArrayList<String> GetLines(ScoreboardManager manager, Player player)
	{
		ArrayList<String> orderedScores = new ArrayList<String>();
		
		orderedScores.add(_line);
		
		return orderedScores;
	}
	
}
