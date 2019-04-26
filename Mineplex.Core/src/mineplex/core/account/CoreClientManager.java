package mineplex.core.account;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import mineplex.core.MiniPlugin;
import mineplex.core.account.command.UpdateRank;
import mineplex.core.account.event.ClientUnloadEvent;
import mineplex.core.account.event.ClientWebResponseEvent;
import mineplex.core.account.repository.AccountRepository;
import mineplex.core.account.repository.token.ClientToken;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.timing.TimingManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.serverdata.Region;
import mineplex.serverdata.data.BungeeServer;
import mineplex.serverdata.redis.RedisDataRepository;
import mineplex.serverdata.servers.ServerManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreClientManager extends MiniPlugin
{
	private static NautHashMap<String, Object> _clientLoginLock = new NautHashMap<String, Object>();
	
	private JavaPlugin _plugin;
	private AccountRepository _repository;
	private NautHashMap<String, CoreClient> _clientList;
	private HashSet<String> _duplicateLoginGlitchPreventionList;
	private RedisDataRepository<AccountCache> _accountCacheRepository;
	
	private NautHashMap<String, ILoginProcessor> _loginProcessors = new NautHashMap<String, ILoginProcessor>();
	
	private Object _clientLock = new Object();
	
	private static AtomicInteger _clientsConnecting = new AtomicInteger(0);
	private static AtomicInteger _clientsProcessing = new AtomicInteger(0);
	
	public CoreClientManager(JavaPlugin plugin, String webServer)
	{
		super("Client Manager", plugin);
		
		_plugin = plugin;
		_repository = new AccountRepository(plugin, webServer);
        _clientList = new NautHashMap<String, CoreClient>();
        _duplicateLoginGlitchPreventionList = new HashSet<String>();
        
        _accountCacheRepository = new RedisDataRepository<AccountCache>(ServerManager.getMasterConnection(), ServerManager.getSlaveConnection(),
				Region.ALL, AccountCache.class, "accountCache");
	}
	
	public AccountRepository getRepository()
	{
		return _repository;
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new UpdateRank(this));
	}

	public CoreClient Add(String name)
	{
		CoreClient newClient = null;
	    
		if (newClient == null)
		{
			newClient = new CoreClient(name);
		}
		
		CoreClient oldClient = null;
		
		synchronized(_clientLock)
		{
			oldClient = _clientList.put(name, newClient);
		}
	    
	    if (oldClient != null)
	    {
	    	oldClient.Delete();
	    }

		return newClient;
	}

	public void Del(String name)
	{
		synchronized(_clientLock)
		{
			_clientList.remove(name); 
		}
		
		_plugin.getServer().getPluginManager().callEvent(new ClientUnloadEvent(name));
	}

	public CoreClient Get(String name)
	{
		synchronized(_clientLock)
		{
			return _clientList.get(name);
		}
	}
	
	public CoreClient Get(Player player)
	{
		synchronized(_clientLock)
		{
			return _clientList.get(player.getName());
		}
	}
	
	public int getPlayerCountIncludingConnecting()
	{
		return Bukkit.getOnlinePlayers().size() + Math.max(0, _clientsConnecting.get());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void AsyncLogin(AsyncPlayerPreLoginEvent event)
	{
		try
		{
			_clientsConnecting.incrementAndGet();
			while (_clientsProcessing.get() >= 5)
			{
				try
				{
					Thread.sleep(25);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			try
			{
				_clientsProcessing.incrementAndGet();
				
				if (!LoadClient(Add(event.getName()), event.getUniqueId(), event.getAddress().getHostAddress()))
					event.disallow(Result.KICK_OTHER, "There was a problem logging you in.");
			}
			catch(Exception exception)
			{
				event.disallow(Result.KICK_OTHER, "Error retrieving information from web, please retry in a minute.");
				exception.printStackTrace();
			}
			finally
			{
				_clientsProcessing.decrementAndGet();
			}
			
			if (Bukkit.hasWhitelist() && !Get(event.getName()).GetRank().Has(Rank.MODERATOR))
			{
				for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
				{
					if (player.getName().equalsIgnoreCase(event.getName()))
					{
						return;
					}
				}
				
				event.disallow(Result.KICK_WHITELIST, "You are not whitelisted my friend.");
			}
		}
		finally
		{
			_clientsConnecting.decrementAndGet();
		}
	}
	
	public void loadClientByName(final String playerName, final Runnable runnable)
	{
		Bukkit.getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			public void run()
			{
				try
				{
					ClientToken token = null;
					Gson gson = new Gson();
				
					// Fails if not in DB and if duplicate.
					UUID uuid = loadUUIDFromDB(playerName);
					
					if (uuid == null)
						uuid = UUIDFetcher.getUUIDOf(playerName);
					
					String response = "";
					
					if (uuid == null)
					{
						response = _repository.getClientByName(playerName);
					}
					else
					{
						response = _repository.getClientByUUID(uuid);
					}
					
			        token = gson.fromJson(response, ClientToken.class);
	
			        CoreClient client = Add(playerName);
					client.SetRank(Rank.valueOf(token.Rank));					
					client.setAccountId(_repository.login(_loginProcessors, uuid.toString(), client.GetPlayerName()));
					
					// JSON sql response
					Bukkit.getServer().getPluginManager().callEvent(new ClientWebResponseEvent(response, uuid));
					
					if (client.getAccountId() > 0)
						_accountCacheRepository.addElement(new AccountCache(uuid, client.getAccountId()));
				}
				catch (Exception exception)
				{
					exception.printStackTrace();					
				}
				finally
				{
					Bukkit.getServer().getScheduler().runTask(getPlugin(), new Runnable()
					{
						public void run()
						{
							if (runnable != null)
								runnable.run();
						}
					});
				}
			}
		});
	}
	
	private boolean LoadClient(final CoreClient client, final UUID uuid, String ipAddress)
	{
		TimingManager.start(client.GetPlayerName() + " LoadClient Total.");
		long timeStart = System.currentTimeMillis();

		_clientLoginLock.put(client.GetPlayerName(), new Object());
		ClientToken token = null;
		Gson gson = new Gson();
		
		runAsync(new Runnable()
		{
			public void run()
			{
				client.setAccountId(_repository.login(_loginProcessors, uuid.toString(), client.GetPlayerName()));
				_clientLoginLock.remove(client.GetPlayerName());
			}
		});
		
		TimingManager.start(client.GetPlayerName() + " GetClient.");
		String response = _repository.GetClient(client.GetPlayerName(), uuid, ipAddress);
		TimingManager.stop(client.GetPlayerName() + " GetClient.");
		
        token = gson.fromJson(response, ClientToken.class);

		client.SetRank(Rank.valueOf(token.Rank));
		
		// _repository.updateMysqlRank(uuid.toString(), token.Rank, token.RankPerm, new Timestamp(Date.parse(token.RankExpire)).toString());
		
		// JSON sql response
		Bukkit.getServer().getPluginManager().callEvent(new ClientWebResponseEvent(response, uuid));
		
		while (_clientLoginLock.containsKey(client.GetPlayerName()) && System.currentTimeMillis() - timeStart < 15000)
		{
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		if (_clientLoginLock.containsKey(client.GetPlayerName()))
		{
			System.out.println("MYSQL TOO LONG TO LOGIN....");
		}
		
		TimingManager.stop(client.GetPlayerName() + " LoadClient Total.");
		
		System.out.println(client.GetPlayerName() + "'s account id = " + client.getAccountId());
		
		if (client.getAccountId() > 0)
			_accountCacheRepository.addElement(new AccountCache(uuid, client.getAccountId()));
		
		return !_clientLoginLock.containsKey(client.GetPlayerName());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Login(PlayerLoginEvent event)
	{
		synchronized(_clientLock)
		{
			if (!_clientList.containsKey(event.getPlayer().getName()))
			{
				_clientList.put(event.getPlayer().getName(), new CoreClient(event.getPlayer().getName()));
			}
		}

        CoreClient client = Get(event.getPlayer().getName());
        
        if (client == null || client.GetRank() == null)
        {
        	event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "There was an error logging you in.  Please reconncet.");
        	return;
        }
        
        client.SetPlayer(event.getPlayer());
		
        // Reserved Slot Check
		if (Bukkit.getOnlinePlayers().size() >= Bukkit.getServer().getMaxPlayers())
		{
			if (client.GetRank().Has(event.getPlayer(), Rank.ULTRA, false))
			{
				event.allow();
				event.setResult(PlayerLoginEvent.Result.ALLOWED);
				return;
			}
			
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "This server is full and no longer accepts players.");
		}
	}
	
	@EventHandler
	public void Kick(PlayerKickEvent event)
	{
		if (event.getReason().contains("You logged in from another location"))
		{
			_duplicateLoginGlitchPreventionList.add(event.getPlayer().getName());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void Quit(PlayerQuitEvent event)
	{
		// When an account is logged in to this server and the same account name logs in
		// Then it Fires events in this order  (original, new for accounts)
		// AsyncPreLogin -> new
		// PlayerLogin -> new
		// PlayerKick -> old
		// PlayerQuit -> old
		// Then it glitches because it added new, but then removed old afterwards since its based on name as key.
		
		if (!_duplicateLoginGlitchPreventionList.contains(event.getPlayer().getName()))
		{
			Del(event.getPlayer().getName());
			_duplicateLoginGlitchPreventionList.remove(event.getPlayer().getName());
		}
	}

	public void SaveRank(final String name, final UUID uuid, Rank rank, boolean perm)
	{
		_repository.saveRank(new Callback<Rank>()
		{
			public void run(Rank newRank)
			{
				if (_plugin.getServer().getPlayer(name) != null)
				{
					CoreClient client = Get(name);				

					client.SetRank(newRank);
				}
			}
		}, name, uuid, rank, perm);
	}
	
	public void checkPlayerNameExact(final Callback<Boolean> callback, final String playerName)
	{
		_repository.matchPlayerName(new Callback<List<String>>()
		{
			public void run(List<String> matches)
			{
				for (String match : matches)
				{
					if (match.equalsIgnoreCase(playerName))
					{
						callback.run(true);
					}
				}

				callback.run(false);
			}
		}, playerName);
	}
	
	public void checkPlayerName(final Player caller, final String playerName, final Callback<String> callback)
	{
		_repository.matchPlayerName(new Callback<List<String>>()
		{
			public void run(List<String> matches)
			{
				String tempName = null;
				
				for (String match : matches)
				{
					if (match.equalsIgnoreCase(playerName))
					{
						tempName = match;
						break;
					}
				}
				
				final String matchedName = tempName;
				
				if (matchedName != null)
				{
					for (Iterator<String> matchIterator = matches.iterator(); matchIterator.hasNext();)
					{
						if (!matchIterator.next().equalsIgnoreCase(playerName))
						{
							matchIterator.remove();
						}
					}
				}
				
				UtilPlayer.searchOffline(matches, new Callback<String>()
				{
					public void run(final String target)
					{
						if (target == null)
						{
							callback.run(matchedName);
							return;
						}
						
						callback.run(matchedName);
					}
				}, caller, playerName, true);
			}
		}, playerName);
	}
	
	// DONT USE THIS IN PRODUCTION...its for enjin listener -someone you despise but definitely not me (defek7)
	public UUID loadUUIDFromDB(String name)
	{
		return _repository.getClientUUID(name);
	}
	
	@EventHandler
	public void cleanGlitchedClients(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		synchronized(_clientLock)
		{
			for (Iterator<Entry<String, CoreClient>> clientIterator = _clientList.entrySet().iterator(); clientIterator.hasNext();)
			{
				Player clientPlayer = clientIterator.next().getValue().GetPlayer();
				
				if (clientPlayer != null && !clientPlayer.isOnline())
				{
					clientIterator.remove();
					
					if (clientPlayer != null)
						_plugin.getServer().getPluginManager().callEvent(new ClientUnloadEvent(clientPlayer.getName()));
				}
			}
		}
	}
	
	@EventHandler
	public void debug(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWER)
			return;
		
//		System.out.println("=====");
//		System.out.println("Connecting  : " + _clientsConnecting.get());
//		System.out.println("Processing  : " + _clientsProcessing.get());
//		System.out.println("=====");
	}
	
	public void addStoredProcedureLoginProcessor(ILoginProcessor processor)
	{
		_loginProcessors.put(processor.getName(), processor);
	}
	
	public boolean hasRank(Player player, Rank rank)
	{
		CoreClient client = Get(player);
		if (client == null)
			return false;
		
		return client.GetRank().Has(rank);
	}
	
	public int getCachedClientAccountId(UUID uuid)
	{
		return _accountCacheRepository.getElement(uuid.toString()).getId();
	}
}