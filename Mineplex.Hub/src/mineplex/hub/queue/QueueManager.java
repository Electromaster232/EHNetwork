package mineplex.hub.queue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.donation.DonationManager;
import mineplex.core.elo.EloManager;
import mineplex.core.party.PartyManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class QueueManager extends MiniPlugin
{
	private static Object _queueLock = new Object();
	private static Object _queuePlayerListLock = new Object();
	
	private EloManager _eloManager;
	private PartyManager _partyManager;
	private QueueRepository _repository;
	private NautHashMap<String, PlayerMatchStatus> _queuedPlayerMatchList = new NautHashMap<String, PlayerMatchStatus>();
	
	private HashSet<String> _queueingPlayers = new HashSet<String>();
	
	public QueueManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, EloManager eloManager, PartyManager partyManager)
	{
		super("Queue Manager", plugin);
		
		setupConfigValues();
		
		_eloManager = eloManager;
		_partyManager = partyManager;
		_repository = new QueueRepository(plugin.getConfig().getBoolean("queue.us"));
	}
	
	private void setupConfigValues()
	{
	    try 
	    {
			getPlugin().getConfig().addDefault("queue.us", true);
			getPlugin().getConfig().set("queue.us", getPlugin().getConfig().getBoolean("queue.us"));
	    }   
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public boolean isQueued(Player player)
	{
		synchronized (_queueLock)
		{
			return _queuedPlayerMatchList.containsKey(player.getName());
		}
	}
	
	public PlayerMatchStatus getQueuedPlayerStatus(Player player)
	{
		synchronized (_queueLock)
		{
			return _queuedPlayerMatchList.get(player.getName());
		}
	}
	
	public void queuePlayer(final String gameType, final Player...players)
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		for (Player player : players)
		{
			stringBuilder.append(player.getName() + ", ");
		}
		
		final String playerList = stringBuilder.toString().substring(0, stringBuilder.length() - 2);
		
		// Prevent spamming it up.
		synchronized (_queuePlayerListLock)
		{
			for (Player player : players)
			{
				if (_queueingPlayers.contains(player.getName()))
					return;
			}
			
			for (Player player : players)
			{
				_queueingPlayers.add(player.getName());
			}
		}
		
		int eloCumulative = 0;
		
		for (Player player : players)
		{
			eloCumulative = _eloManager.getElo(player.getUniqueId(), gameType);
		}
		
		final int elo = eloCumulative / players.length;
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				PlayerMatchStatus matchStatus = _repository.addQueueRecord(playerList, players.length, gameType, elo);
				
				synchronized (_queueLock)
				{
					_queuedPlayerMatchList.put(players[0].getName(), matchStatus);					
				}
				
				synchronized (_queuePlayerListLock)
				{
					for (Player player : players)
					{
						_queueingPlayers.remove(player.getName());
					}
				}
			}
		});
	}
	
	@EventHandler
	public void removeDisconnectingPlayer(PlayerQuitEvent event)
	{
		synchronized (_queueLock)
		{
			final PlayerMatchStatus matchStatus = _queuedPlayerMatchList.remove(event.getPlayer().getName());
			
			if (matchStatus != null)
			{
				Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
				{
					public void run()
					{
						_repository.deleteQueueRecord(matchStatus);
					}
				});
			}
		}
	}
	
	@EventHandler
	public void updateQueues(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		final NautHashMap<String, PlayerMatchStatus> queuedPlayerMatchCopy = new NautHashMap<String, PlayerMatchStatus>();
		
		synchronized (_queueLock)
		{
			for (Entry<String, PlayerMatchStatus> entry : _queuedPlayerMatchList.entrySet())
			{
				queuedPlayerMatchCopy.put(entry.getKey(), entry.getValue());
			}
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{				
				for (Iterator<Entry<String, PlayerMatchStatus>> iterator = queuedPlayerMatchCopy.entrySet().iterator(); iterator.hasNext();)
				{
					Entry<String, PlayerMatchStatus> entry = iterator.next();
					
					boolean prompted = entry.getValue().Prompted;
					PlayerMatchStatus newStatus = _repository.updateQueueStatus(entry.getValue().Id);
					
					synchronized (_queueLock)
					{
						// Player queue got removed due to match or he hit cancel.
						if (newStatus == null)
							_queuedPlayerMatchList.remove(entry.getKey());
						else
						{
							if (newStatus.AssignedMatch != -1)
								newStatus = _repository.updateOtherPlayersMatchStatus(newStatus);
								
							
							newStatus.Prompted = prompted;
							_queuedPlayerMatchList.put(entry.getKey(), newStatus);
						}
					}
				}
			}
		});
	}
	
	public List<Player> findPlayersNeedingPrompt()
	{
		List<Player> players = new ArrayList<Player>();
		
		synchronized (_queueLock)
		{
			for (Entry<String, PlayerMatchStatus> entry : _queuedPlayerMatchList.entrySet())
			{
				if (entry.getValue().AssignedMatch != -1 && !entry.getValue().Prompted)
				{
					Player player = Bukkit.getPlayer(entry.getKey());
					
					if (player == null)
						continue;
					
					players.add(player);
					entry.getValue().Prompted = true;
				}
			}
		}
		
		return players;
	}

	public void respondToInvite(final Player player, final boolean accepted)
	{
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				PlayerMatchStatus matchStatus = null;
				
				synchronized (_queueLock)
				{
					matchStatus = _queuedPlayerMatchList.get(player.getName());
					
					if (!accepted)
						_queuedPlayerMatchList.remove(player.getName());
				}
				
				if (matchStatus == null)
					return;
				
				matchStatus.State = accepted ? "Ready" : "Denied";
				
				_repository.updateState(matchStatus);
				
				if (!accepted)
					_repository.deleteQueueRecord(matchStatus);
			}
		});
	}

	public EloManager getEloManager()
	{
		return _eloManager;
	}

	public PartyManager getPartyManager()
	{
		return _partyManager;
	}
}
