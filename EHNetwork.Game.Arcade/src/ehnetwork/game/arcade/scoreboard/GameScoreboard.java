package ehnetwork.game.arcade.scoreboard;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;

public class GameScoreboard
{
	private Game Game;

	private Scoreboard _scoreboard;
	private Objective _sideObjective;

	private ArrayList<ScoreboardElement> _elements = new ArrayList<ScoreboardElement>();
	private String[] _current = new String[15];

	private String _title;
	private int _shineIndex;
	private boolean _shineDirection = true;
	
	private boolean _debug = false;
	
	public GameScoreboard(Game game)
	{
		Game = game;

		_title = "   EHNETWORK   ";

		//Scoreboard
		_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		_sideObjective = _scoreboard.registerNewObjective("Obj"+UtilMath.r(999999999), "dummy");
		_sideObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		_sideObjective.setDisplayName(C.Bold + _title);
	}

	public Scoreboard GetScoreboard()
	{
		return _scoreboard;
	}

	public Objective GetObjectiveSide()
	{
		return _sideObjective;
	}

	public void UpdateTitle()
	{
		String out;
		
		if (_shineDirection)
		{
			out = C.cGold + C.Bold;
		}
		else
		{
			out = C.cWhite + C.Bold;
		}
		
		for (int i=0 ; i < _title.length() ; i++)
		{
			char c = _title.charAt(i);
			
			if (_shineDirection)
			{
				if (i == _shineIndex)
					out += C.cYellow + C.Bold;
				
				if (i == _shineIndex + 1)
					out += C.cWhite + C.Bold;
			}
			else
			{
				if (i == _shineIndex)
					out += C.cYellow + C.Bold;
				
				if (i == _shineIndex + 1)
					out += C.cGold + C.Bold;
			}
			
			
			out += c;
		}
		
		_sideObjective.setDisplayName(out);
		
		_shineIndex++;
		
		if (_shineIndex == _title.length()*2)
		{
			_shineIndex = 0;
			_shineDirection = !_shineDirection;
		}
	}

	public String ParseTeamName(String name)
	{
		return name.substring(0, Math.min(16, name.length()));
	}

	public void CreateTeams()
	{
		System.out.println("Creating Scoreboard Teams.");

		
		_scoreboard.registerNewTeam(ParseTeamName("SPEC")).setPrefix(ChatColor.GRAY + "");
		
		//Team Groups
		for (GameTeam team : Game.GetTeamList())
		{
			System.out.println("Scoreboard Team: " + team.GetName().toUpperCase());
			if(team.GetDisplaytag())
			{
				_scoreboard.registerNewTeam(ParseTeamName(team.GetName().toUpperCase())).setPrefix(team.GetColor() + C.Bold + team.GetName() + team.GetColor() + " ");
			}
			else
			{
				_scoreboard.registerNewTeam(ParseTeamName(team.GetName().toUpperCase())).setPrefix(team.GetColor() + "");
			}	
		}
		
		/*
		//Base Groups
		for (Rank rank : Rank.values())
		{
			//_scoreboard.registerNewTeam(ParseTeamName(rank.Name + "SPEC")).setPrefix(ChatColor.GRAY + "");
		}

		//Team Groups
		for (GameTeam team : Game.GetTeamList())
		{
			System.out.println("Scoreboard Team: " + team.GetName().toUpperCase());
			
			for (Rank rank : Rank.values())
			{
				_scoreboard.registerNewTeam(ParseTeamName(rank.Name + team.GetName().toUpperCase())).setPrefix(team.GetColor() + "");
			}	
		}
		*/
	}

	public void SetPlayerTeam(Player player, String teamName) 
	{
		for (Team team : _scoreboard.getTeams())
			team.removePlayer(player);

		if (teamName == null)
			teamName = "";

		String team = ParseTeamName(teamName);

		try
		{
			_scoreboard.getTeam(team).addPlayer(player);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("ERROR ADDING PLAYER TO TEAM: " + team);
		}
	}

	public void ResetScore(String line)
	{
		_scoreboard.resetScores(line);
	}

	/*
	public void Reset()
	{
		for (ScoreboardElement elem : _elements)
		{
			for (String line : elem.GetLines())
			{
				ResetScore(line);
			}
		}

		_elements.clear();

		_space = " ";
	}
	 */

	public String Clean(String line)
	{
		if (line.length() > 16)
			line = line.substring(0, 16);

		return line;
	}

	public void Write(String line)
	{
		line = Clean(line);

		_elements.add(new ScoreboardElementText(line));
	}

	public void WriteOrdered(String key, String line, int value, boolean prependScore)
	{
		if (prependScore)
			line = value + " " + line;

		line = Clean(line);

		for (ScoreboardElement elem : _elements)
		{
			if (elem instanceof ScoreboardElementScores)
			{
				ScoreboardElementScores scores = (ScoreboardElementScores)elem;

				if (scores.IsKey(key))
				{
					scores.AddScore(line, value);
					return;
				}
			}
		}

		_elements.add(new ScoreboardElementScores(key, line, value, true));
	}

	public void WriteBlank()
	{
		_elements.add(new ScoreboardElementText(" "));
	}

	public void Draw()
	{		
		if (_debug)		System.out.println();
		if (_debug)		System.out.println("/////////////////////////");

		//Generate Lines
		ArrayList<String> newLines = new ArrayList<String>();

		for (ScoreboardElement elem : _elements)
		{
			for (String line : elem.GetLines())
			{
				//Ensure no duplicate lines
				while (true)
				{
					boolean matched = false;

					for (String otherLine : newLines)
					{
						if (line.equals(otherLine))
						{
							line += ChatColor.RESET;
							matched = true;
						}
					}

					if (!matched)
						break;
				}

				newLines.add(line);
			}
		}

		//Find Changes
		HashSet<Integer> toAdd = new HashSet<Integer>();
		HashSet<Integer> toDelete = new HashSet<Integer>();

		for (int i=0 ; i<15 ; i++)
		{
			//Delete Old Excess Row
			if (i >= newLines.size())
			{
				if (_current[i] != null)
				{
					if (_debug)		System.out.println("Delete: " + i + " [" + _current[i] + "]");
					toDelete.add(i);
				}

				continue;
			}

			//Update or Add Row
			if (_current[i] == null || !_current[i].equals(newLines.get(i)))
			{
				if (_debug)		System.out.println("Update: " + i + " [" + newLines.get(i) + "]");
				toDelete.add(i);
				toAdd.add(i);
			}
		}

		//Delete Elements - Must happen before Add
		for (int i : toDelete)
		{
			//Remove Old Line at Index
			if (_current[i] != null) 
			{
				if (_debug)		System.out.println("Deleting: " + i + " [" + _current[i] + "]");

				ResetScore(_current[i]);
				_current[i] = null;
			}
		}

		//Add Elements
		for (int i : toAdd)
		{
			//Insert New Line
			String newLine = newLines.get(i);
			GetObjectiveSide().getScore(newLine).setScore(15-i);
			_current[i] = newLine;

			if (_debug)		System.out.println("Setting: " + (15-i) + " [" + newLine + "]");
		}
	}

	public void Reset()
	{
		_elements.clear();
	}
}
