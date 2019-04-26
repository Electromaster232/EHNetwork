package mineplex.core.party;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import mineplex.core.common.Rank;
import mineplex.core.common.jsonchat.ChildJsonMessage;
import mineplex.core.common.jsonchat.ClickEvent;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.party.redis.RedisPartyData;
import mineplex.serverdata.Region;
import mineplex.serverdata.commands.ServerTransfer;
import mineplex.serverdata.commands.TransferCommand;
import mineplex.serverdata.data.ServerGroup;
import mineplex.serverdata.servers.ServerManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Party
{
	private PartyManager _manager;
	private boolean _isHub;

	private String _creator;
	private String _previousServer;

	private ArrayList<String> _players = new ArrayList<String>();
	private NautHashMap<String, Long> _invitee = new NautHashMap<String, Long>();

	private Scoreboard _scoreboard;
	private Objective _scoreboardObj;
	private ArrayList<String> _scoreboardLast = new ArrayList<String>();

	private long _partyOfflineTimer = -1;
	private long _informNewLeaderTimer = -1;

	public Party(PartyManager manager, RedisPartyData partyData)
	{
		this(manager);

		_players = new ArrayList(Arrays.asList(partyData.getPlayers()));
		_creator = partyData.getLeader();
		_previousServer = partyData.getPreviousServer();
	}

	public Party(PartyManager manager)
	{
		_manager = manager;
		Region region = manager.getPlugin().getConfig().getBoolean("serverstatus.us") ? Region.US : Region.EU;
		String groupName = manager.getPlugin().getConfig().getString("serverstatus.group");

		ServerGroup serverGroup = ServerManager.getServerRepository(region).getServerGroup(groupName);

		if (serverGroup == null)
			return;

		_isHub = !serverGroup.getArcadeGroup();

		if (_isHub)
		{
			// Scoreboard
			_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			_scoreboardObj = _scoreboard.registerNewObjective("Party", "dummy");
			_scoreboardObj.setDisplaySlot(DisplaySlot.SIDEBAR);

			_scoreboard.registerNewTeam(ChatColor.GREEN + "Members");

			// Scoreboard Ranks
			for (Rank rank : Rank.values())
			{
				if (rank != Rank.ALL)
					_scoreboard.registerNewTeam(rank.Name).setPrefix(rank.GetTag(true, false) + ChatColor.RESET + " ");
				else
					_scoreboard.registerNewTeam(rank.Name).setPrefix("");
			}

			_scoreboard.registerNewTeam("Party").setPrefix(ChatColor.LIGHT_PURPLE + C.Bold + "Party" + ChatColor.RESET + " ");

			// Add Players
			for (Player player : Bukkit.getOnlinePlayers())
			{
				_scoreboard.getTeam(_manager.GetClients().Get(player).GetRank().Name).addPlayer(player);
			}
		}
	}

	public void JoinParty(Player player)
	{
		// Add Leader
		if (_players.isEmpty())
		{
			_players.add(player.getName());

			UtilPlayer.message(player, F.main("Party", "You created a new Party."));

			_creator = player.getName();
		}
		else
		{
			_players.add(player.getName());
			_invitee.remove(player.getName());

			Announce(F.elem(player.getName()) + " has joined the party!");
		}

		if (_isHub)
		{
			_scoreboard.getTeam("Party").addPlayer(player);
		}
	}

	public void InviteParty(Player player, boolean inviteeInParty)
	{
		_invitee.put(player.getName(), System.currentTimeMillis());

		// Decline
		if (_players.contains(player.getName()))
		{
			UtilPlayer.message(player, F.main("Party", F.name(player.getName()) + " is already in the Party."));
			player.playSound(player.getLocation(), Sound.NOTE_BASS, 1f, 1.5f);
		}

		// Announce
		Announce(F.name(player.getName()) + " has been invited to your Party.");

		// Inform
		UtilPlayer.message(player, F.main("Party", F.name(GetLeader()) + " invited you to their Party."));

		// Instruct
		if (inviteeInParty)
		{
			ChildJsonMessage message = new JsonMessage("").extra(C.mHead + "Party> " + C.mBody + "Type ");

			message.add(F.link("/party leave")).click(ClickEvent.RUN_COMMAND, "/party leave");

			message.add(C.mBody + " then ");

			message.add(F.link("/party " + GetLeader())).click(ClickEvent.RUN_COMMAND, "/party " + GetLeader());

			message.add(C.mBody + " to join.");

			message.sendToPlayer(player);
		}
		else
		{
			ChildJsonMessage message = new JsonMessage("").extra(C.mHead + "Party> " + C.mBody + "Type ");

			message.add(F.link("/party " + GetLeader())).click(ClickEvent.RUN_COMMAND, "/party " + GetLeader());

			message.add(C.mBody + " to join.");

			message.sendToPlayer(player);
		}

		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1.5f);
	}

	public void LeaveParty(Player player)
	{
		// Announce
		Announce(F.name(player.getName()) + " has left the Party.");

		boolean leader = player.equals(GetLeader());

		_players.remove(player.getName());

		if (_isHub)
		{
			// Set Scoreboard
			_scoreboard.getTeam(_manager.GetClients().Get(player).GetRank().Name).addPlayer(player);
		}

		if (leader && _players.size() > 0)
		{
			Announce("Party Leadership passed on to " + F.name(GetLeader()) + ".");
		}
	}

	public void KickParty(String player)
	{
		// Announce
		Announce(F.name(player) + " was kicked from the Party.");

		_players.remove(player);
	}

	public void PlayerJoin(Player player)
	{
		if (_isHub)
		{
			// Scoreboard
			if (_players.contains(player.getName()))
				_scoreboard.getTeam("Party").addPlayer(player);
			else if (_manager.GetClients().Get(player) != null)
				_scoreboard.getTeam(_manager.GetClients().Get(player).GetRank().Name).addPlayer(player);
		}

		if (_creator.equals(player.getName()))
		{
			_players.remove(player.getName());
			_players.add(0, player.getName());

			if (_informNewLeaderTimer < System.currentTimeMillis())
			{
				Announce("Party Leadership returned to " + F.name(GetLeader()) + ".");
			}

			if (_previousServer != null)
			{
				for (String playerName : _players)
				{
					Player p = Bukkit.getPlayerExact(playerName);

					if (p != null)
					{
						continue;
					}

					TransferCommand transferCommand = new TransferCommand(
							new ServerTransfer(playerName, _manager.getServerName()));

					transferCommand.setTargetServers(_previousServer);

					transferCommand.publish();
				}

				_previousServer = null;
			}
		}
	}

	// Shuffle Leader
	public void PlayerQuit(Player player)
	{
		if (player.getName().equals(GetLeader()))
		{
			_players.remove(player.getName());
			_players.add(player.getName());

			if (_informNewLeaderTimer < System.currentTimeMillis())
			{
				Announce("Party Leadership passed on to " + F.name(GetLeader()) + ".");
			}
		}
	}

	public void Announce(String message)
	{
		for (String name : _players)
		{
			Player player = UtilPlayer.searchExact(name);

			if (player != null && player.isOnline())
			{
				UtilPlayer.message(player, F.main("Party", message));
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1.5f);
			}
		}
	}

	public void ExpireInvitees()
	{
		Iterator<String> inviteeIterator = _invitee.keySet().iterator();

		while (inviteeIterator.hasNext())
		{
			String name = inviteeIterator.next();

			if (UtilTime.elapsed(_invitee.get(name), 60000))
			{
				Announce(F.name(name) + " did not respond to the Party invite.");
				inviteeIterator.remove();
			}
		}
	}

	public String GetLeader()
	{
		if (_players.isEmpty())
			return _creator;

		return _players.get(0);
	}

	public Collection<String> GetPlayers()
	{
		return _players;
	}

	public Collection<Player> GetPlayersOnline()
	{
		ArrayList<Player> players = new ArrayList<Player>();

		for (String name : _players)
		{
			Player player = UtilPlayer.searchExact(name);
			if (player != null)
				players.add(player);
		}

		return players;
	}

	public Collection<String> GetInvitees()
	{
		return _invitee.keySet();
	}

	public void UpdateScoreboard()
	{
		if (_isHub)
		{
			_scoreboardObj.setDisplayName(GetLeader() + "'s Party");

			// Clear Past
			for (String pastLine : _scoreboardLast)
				_scoreboard.resetScores(pastLine);
			_scoreboardLast.clear();

			int i = 16;

			// Add New
			for (int j = 0; j < _players.size(); j++)
			{
				String name = _players.get(j);
				Player player = UtilPlayer.searchExact(name);

				ChatColor col = ChatColor.GREEN;
				if (player == null)
					col = ChatColor.RED;

				String line = col + name;

				if (line.length() > 16)
					line = line.substring(0, 16);

				_scoreboardObj.getScore(line).setScore(i);

				_scoreboardLast.add(line);

				i--;
			}

			// Add New
			for (String name : _invitee.keySet())
			{
				int time = 1 + (int) ((60000 - (System.currentTimeMillis() - _invitee.get(name))) / 1000);

				String line = time + " " + ChatColor.GRAY + name;

				if (line.length() > 16)
					line = line.substring(0, 16);

				_scoreboardObj.getScore(line).setScore(i);

				_scoreboardLast.add(line);

				i--;
			}

			// Set Scoreboard
			for (String name : _players)
			{
				Player player = UtilPlayer.searchExact(name);

				if (player != null)
				{
					if (!player.getScoreboard().equals(_scoreboard))
					{
						player.setScoreboard(_scoreboard);
					}
				}
			}
		}
	}

	public boolean IsDead()
	{
		if (_players.size() == 0)
			return true;

		if (_players.size() == 1 && _invitee.size() == 0)
			return true;

		int online = 0;
		for (String name : _players)
		{
			Player player = UtilPlayer.searchExact(name);
			if (player != null)
				online++;
		}

		// One or Less Members Online - Expirey Countdown
		if (online <= 1)
		{
			if (_partyOfflineTimer == -1)
			{
				_partyOfflineTimer = System.currentTimeMillis();
			}
			else
			{
				if (UtilTime.elapsed(_partyOfflineTimer, online == 0 ? 5000 : 120000)) // 5 seconds for no players, 2 minutes if
																						// one player.
				{
					return true;
				}
			}
		}
		else if (_partyOfflineTimer > 0)
		{
			_partyOfflineTimer = -1;
		}

		return false;
	}

	public void resetWaitingTime()
	{
		_partyOfflineTimer = -1;
	}

	public void switchedServer()
	{
		_informNewLeaderTimer = System.currentTimeMillis() + 5000;
	}
}
