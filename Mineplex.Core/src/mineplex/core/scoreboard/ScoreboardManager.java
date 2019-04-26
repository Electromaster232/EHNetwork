package mineplex.core.scoreboard;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class ScoreboardManager extends MiniPlugin
{	
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;

	//This stores current scoreboard for the player
	private HashMap<Player, PlayerScoreboard> _playerScoreboards = new HashMap<Player, PlayerScoreboard>();

	//Scoreboards (can be shared between players)
	private HashMap<String, ScoreboardData> _scoreboards = new HashMap<String, ScoreboardData>();

	//Title
	private String _title = "   EHNETWORK   ";
	private int _shineIndex;
	private boolean _shineDirection = true;

	public ScoreboardManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager)
	{
		super("Scoreboard Manager", plugin);

		_clientManager = clientManager;
		_donationManager = donationManager;
	}

	public CoreClientManager getClients()
	{
		return _clientManager;
	}

	public DonationManager getDonation()
	{
		return _donationManager;
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		_playerScoreboards.put(event.getPlayer(), new PlayerScoreboard(this, event.getPlayer()));
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_playerScoreboards.remove(event.getPlayer());
	}

	public void draw()
	{
		Iterator<Player> playerIterator = _playerScoreboards.keySet().iterator();

		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();

			//Offline
			if (!player.isOnline())
			{
				playerIterator.remove();
				continue;
			}

			_playerScoreboards.get(player).draw(this, player);
		}
	}


	
	public ScoreboardData getData(String scoreboardName, boolean create)
	{
		if (!create)
			return _scoreboards.get(scoreboardName);

		if (!_scoreboards.containsKey(scoreboardName))
			_scoreboards.put(scoreboardName, new ScoreboardData());

		return _scoreboards.get(scoreboardName);
	}
	
	@EventHandler
	public void updateTitle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		String out;

		if (_shineDirection)
		{
			out = C.cGold + C.Bold;
		}
		else
		{
			out = C.cWhite + C.Bold;
		}

		for (int i = 0; i < _title.length(); i++)
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

		for (PlayerScoreboard ps : _playerScoreboards.values())
		{
			ps.setTitle(out); 
		}
	
		_shineIndex++;

		if (_shineIndex == _title.length() * 2)
		{
			_shineIndex = 0;
			_shineDirection = !_shineDirection;
		}
	}
}
