package ehnetwork.core.scoreboard.elements;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import ehnetwork.core.scoreboard.ScoreboardManager;

public class ScoreboardElementScores extends ScoreboardElement
{
	private String _key;
	
	private HashMap<String, Integer> _scores;
	
	private boolean _higherIsBetter;
	
	public ScoreboardElementScores(String key, String line, int value, boolean higherIsBetter)
	{
		_scores = new HashMap<String, Integer>();
		
		_key = key;
		
		AddScore(line, value);
		
		_higherIsBetter = higherIsBetter;
	}
	
	@Override
	public ArrayList<String> GetLines(ScoreboardManager manager, Player player)
	{
		ArrayList<String> orderedScores = new ArrayList<String>();
		
		//Order Scores
		while (orderedScores.size() < _scores.size())
		{
			String bestKey = null;
			int bestScore = 0;
			
			for (String key : _scores.keySet())
			{
				if (orderedScores.contains(key))
					continue;
				
				if (bestKey == null || 
						(_higherIsBetter && _scores.get(key) >= bestScore) ||
						(!_higherIsBetter && _scores.get(key) <= bestScore))
				{
					bestKey = key;
					bestScore = _scores.get(key);
				}
			}
			
			orderedScores.add(bestKey);
		}
		
		return orderedScores;
	}

	public boolean IsKey(String key)
	{
		return _key.equals(key);
	}

	public void AddScore(String line, int value)
	{
		_scores.put(line, value);
	}
}
