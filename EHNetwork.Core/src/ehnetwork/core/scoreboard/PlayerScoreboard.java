package ehnetwork.core.scoreboard;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilMath;

public class PlayerScoreboard
{
	private ScoreboardManager _manager;

	private String _scoreboardData = "default";

	private Scoreboard _scoreboard;
	private Objective _sideObjective;

	private ArrayList<String> _currentLines = new ArrayList<String>();

	private String[] _teamNames;

	public PlayerScoreboard(ScoreboardManager manager, Player player)
	{
		_manager = manager;


	}

	private void addTeams(Player player)
	{
		for (Rank rank : Rank.values())
		{
			if (rank != Rank.ALL)
				_scoreboard.registerNewTeam(rank.Name).setPrefix(rank.GetTag(true, true) + ChatColor.RESET + " ");
			else
				_scoreboard.registerNewTeam(rank.Name).setPrefix("");
		}

		_scoreboard.registerNewTeam("Party").setPrefix(ChatColor.LIGHT_PURPLE + C.Bold + "Party" + ChatColor.RESET + " ");

		for (Player otherPlayer : Bukkit.getOnlinePlayers())
		{
			if (_manager.getClients().Get(otherPlayer) == null)
				continue;

			String rankName = _manager.getClients().Get(player).GetRank().Name;
			String otherRankName = _manager.getClients().Get(otherPlayer).GetRank().Name;

			if (!_manager.getClients().Get(player).GetRank().Has(Rank.ULTRA) && _manager.getDonation().Get(player.getName()).OwnsUltraPackage())
			{
				rankName = Rank.ULTRA.Name;
			}

			if (!_manager.getClients().Get(otherPlayer).GetRank().Has(Rank.ULTRA) && _manager.getDonation().Get(otherPlayer.getName()).OwnsUltraPackage())
			{
				otherRankName = Rank.ULTRA.Name;
			}

			//Add Other to Self
			_scoreboard.getTeam(otherRankName).addPlayer(otherPlayer);

			//Add Self to Other
			otherPlayer.getScoreboard().getTeam(rankName).addPlayer(player);
		}
	}

	private ScoreboardData getData()
	{
		ScoreboardData data = _manager.getData(_scoreboardData, false);
		if (data != null)
			return data;

		//Revert to default
		_scoreboardData = "default";
		return _manager.getData(_scoreboardData, false);
	}

	public void draw(ScoreboardManager manager, Player player)
	{
		ScoreboardData data = getData();

		if (data == null)
			return;

		for (int i=0 ; i<data.getLines(manager, player).size() ; i++)
		{
			//Get New Line
			String newLine = data.getLines(manager, player).get(i);

			//Check if Unchanged
			if (_currentLines.size() > i)
			{
				String oldLine = _currentLines.get(i);

				if (oldLine.equals(newLine))
					continue;
			}

			//Update
			Team team = _scoreboard.getTeam(_teamNames[i]);
			if (team == null)
			{
				System.out.println("Scoreboard Error: Line Team Not Found!");
				return;
			}

			//Set Line Prefix/Suffix
			team.setPrefix(newLine.substring(0, Math.min(newLine.length(), 16)));
			team.setSuffix(ChatColor.getLastColors(newLine) + newLine.substring(team.getPrefix().length(), Math.min(newLine.length(), 32)));

			//Line
			_sideObjective.getScore(_teamNames[i]).setScore(15-i);
		}

		//Hide Old Unused
		if (_currentLines.size() > data.getLines(manager, player).size())		
		{
			for (int i=data.getLines(manager, player).size() ; i<_currentLines.size() ; i++)
			{
				_scoreboard.resetScores(_teamNames[i]);
			}
		}

		//Save New State
		_currentLines = data.getLines(manager, player);
	}

	public void setTitle(String out)
	{
		_sideObjective.setDisplayName(out);
	}

	public void assignScoreboard(Player player, ScoreboardData data)
	{
		//Scoreboard
		_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		//Side Obj
		_sideObjective = _scoreboard.registerNewObjective(player.getName() + UtilMath.r(999999999), "dummy");
		_sideObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		_sideObjective.setDisplayName(C.Bold + "   EHNETWORK   ");

		//Teams
		addTeams(player);

		//Create Line Teams - There will always be 16 teams, with static line allocations.
		_teamNames = new String[16];
		for (int i=0 ; i<16 ; i++)
		{
			String teamName = ChatColor.COLOR_CHAR + "" + ("1234567890abcdefghijklmnopqrstuvwxyz".toCharArray())[i] + ChatColor.RESET;

			_teamNames[i] = teamName;

			Team team = _scoreboard.registerNewTeam(teamName);
			team.addEntry(teamName);
		}

		
		
		//
//		if (data.getDisplayRanks())
//		for (Player otherPlayer : Bukkit.getOnlinePlayers())
//		{
//			if (_clientManager.Get(otherPlayer) == null)
//				continue;
//
//			String rankName = _clientManager.Get(player).GetRank().Name;
//			String otherRankName = _clientManager.Get(otherPlayer).GetRank().Name;
//
//			if (!_clientManager.Get(player).GetRank().Has(Rank.ULTRA) && _donationManager.Get(player.getName()).OwnsUltraPackage())
//			{
//				rankName = Rank.ULTRA.Name;
//			}
//
//			if (!_clientManager.Get(otherPlayer).GetRank().Has(Rank.ULTRA) && _donationManager.Get(otherPlayer.getName()).OwnsUltraPackage())
//			{
//				otherRankName = Rank.ULTRA.Name;
//			}
//
//			//Add Other to Self
//			board.getTeam(otherRankName).addPlayer(otherPlayer);
//		}
		
		//Set Scoreboard
		player.setScoreboard(_scoreboard);
	}
}
