package mineplex.hub.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.donation.DonationManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.party.Party;
import mineplex.core.party.PartyManager;
import mineplex.core.portal.Portal;
import mineplex.core.recharge.Recharge;
import mineplex.core.shop.ShopBase;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.HubManager;
import mineplex.hub.commands.ServerNPCCommand;
import mineplex.hub.modules.StackerManager;
import mineplex.hub.queue.QueueManager;
import mineplex.hub.queue.ui.QueueShop;
import mineplex.hub.server.ui.LobbyShop;
import mineplex.hub.server.ui.QuickShop;
import mineplex.hub.server.ui.ServerNpcShop;
import mineplex.serverdata.data.MinecraftServer;
import mineplex.serverdata.data.ServerGroup;

public class ServerManager extends MiniPlugin
{
	private static final Long FREE_PORTAL_TIMER = 20000L;
	private static final Long BETA_PORTAL_TIMER = 120000L;

	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private Portal _portal;
	private PartyManager _partyManager;
	private ServerStatusManager _statusManager;
	private HubManager _hubManager;
	private StackerManager _stackerManager;
	private QueueManager _queueManager;
	
	private NautHashMap<String, HashSet<ServerInfo>> _serverKeyInfoMap = new NautHashMap<String, HashSet<ServerInfo>>();
	private NautHashMap<String, String> _serverKeyTagMap = new NautHashMap<String, String>();
	private NautHashMap<String, Integer> _serverPlayerCounts = new NautHashMap<String, Integer>();
	private NautHashMap<String, ServerNpcShop> _serverNpcShopMap = new NautHashMap<String, ServerNpcShop>();
	private NautHashMap<String, ServerInfo> _serverInfoMap = new NautHashMap<String, ServerInfo>();
	private NautHashMap<String, Long> _serverUpdate = new NautHashMap<String, Long>();
	private NautHashMap<Vector, String> _serverPortalLocations = new NautHashMap<Vector, String>();

	// Join Time for Free Players Timer
	private NautHashMap<String, Long> _joinTime = new NautHashMap<String, Long>();

	private QueueShop _domShop;
	private QuickShop _quickShop;
	private LobbyShop _lobbyShop;
	
	private boolean _alternateUpdateFire = false;
	private boolean _retrieving = false;
	private long _lastRetrieve = 0;
	
	public ServerManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, Portal portal, PartyManager partyManager, ServerStatusManager statusManager, HubManager hubManager, StackerManager stackerManager, QueueManager queueManager)
	{
		super("Server Manager", plugin);
		
		_clientManager = clientManager;
		_donationManager = donationManager;
		_portal = portal;
		_partyManager = partyManager;
		_statusManager = statusManager;
		_hubManager = hubManager;
		_stackerManager = stackerManager;
		_queueManager = queueManager;

		addCommand(new ServerNPCCommand(this));
		
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		
		LoadServers();
		
		_quickShop = new QuickShop(this, clientManager, donationManager, "Quick Menu");
		_lobbyShop = new LobbyShop(this, clientManager, donationManager, "Lobby Menu");
		//_domShop = new new QueueShop(_queueManager, clientManager, donationManager, "Dominate");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerPortalEvent(PlayerPortalEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void entityPortalEvent(EntityPortalEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerCheckPortalEvent(EntityPortalEnterEvent event)
	{
		if (!(event.getEntity() instanceof Player))
		{
			if (event.getEntity() instanceof LivingEntity)
				UtilAction.velocity(event.getEntity(), UtilAlg.getTrajectory(event.getEntity().getLocation(), _hubManager.GetSpawn()), 1, true, 0.8, 0, 1, true);
			
			return;
		}
		
		final Player player = (Player)event.getEntity();

		if (!Recharge.Instance.use(player, "Portal Server", 1000, false, false))
			return;

		long timeUntilPortal = getMillisecondsUntilPortal(player, false);
		if (!_hubManager.CanPortal(player) || timeUntilPortal > 0)
		{
			player.closeInventory();
			
			if (timeUntilPortal > 0)
			{
				player.playSound(player.getEyeLocation(), Sound.CHICKEN_EGG_POP, 2, 2);
				UtilPlayer.message(player, F.main("Server Portal", "You cannot join a server for " + C.cGreen + UtilTime.convertString(timeUntilPortal, 0, TimeUnit.SECONDS)));
			}

			UtilAction.velocity(player, UtilAlg.getTrajectory(player.getLocation(), _hubManager.GetSpawn()), 1.5, true, 0.8, 0, 1.0, true);

			// Need to set their velocity again a tick later
			// Setting Y-Velocity while in a portal doesn't seem to do anything... Science!
			_plugin.getServer().getScheduler().runTask(_plugin, new Runnable()
			{

				@Override
				public void run()
				{
					if (player != null && player.isOnline())
					{
						UtilAction.velocity(player, UtilAlg.getTrajectory(player.getLocation(), _hubManager.GetSpawn()), 1, true, 0.5, 0, 1.0, true);
					}
				}
			});

			return;
		}
		
		String serverName = _serverPortalLocations.get(player.getLocation().getBlock().getLocation().toVector());

		if (serverName != null)
		{
			List<ServerInfo> serverList = new ArrayList<ServerInfo>(GetServerList(serverName));
			
			int slots = 1;
			
			if (serverList.size() > 0)
			{
				slots = GetRequiredSlots(player, serverList.get(0).ServerType);
			}

			try
			{
				Collections.sort(serverList, new ServerSorter(slots));
				
				for (ServerInfo serverInfo : serverList)
				{
					if ((serverInfo.MOTD.contains("Starting") || serverInfo.MOTD.contains("Recruiting") || serverInfo.MOTD.contains("Waiting") || serverInfo.MOTD.contains("Cup")) && (serverInfo.MaxPlayers - serverInfo.CurrentPlayers) >= slots)
					{
						SelectServer(player, serverInfo);
						return;
					}
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			
			UtilPlayer.message(player, F.main("Server Portal", "There are currently no joinable servers!"));
		}
	}
	
	@EventHandler
	public void checkQueuePrompts(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		/*
		for (final Player player : _queueManager.findPlayersNeedingPrompt())
		{
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 5f, 1f);
			
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable()
			{
				public void run()
				{
					if (player.isOnline())
					{
						_domShop.attemptShopOpen(player);						
					}
				}
			}, 20);
		}
		*/
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerJoin(PlayerJoinEvent event)
	{
		event.getPlayer().getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.COMPASS.getId(), (byte)0, 1, ChatColor.GREEN + "Game Menu"));
		event.getPlayer().getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WATCH.getId(), (byte)0, 1, ChatColor.GREEN + "Lobby Menu"));

		if (_clientManager.Get(event.getPlayer()).GetRank() == Rank.ALL)
		{
			_joinTime.put(event.getPlayer().getName(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_joinTime.remove(event.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerInteract(PlayerInteractEvent event)
	{
		if (event.getItem() != null && event.getItem().getType() == Material.COMPASS)
		{
			_quickShop.attemptShopOpen(event.getPlayer());
		}
		else if (event.getItem() != null && event.getItem().getType() == Material.WATCH)
		{
			_lobbyShop.attemptShopOpen(event.getPlayer());
		}
	}

	public Long getMillisecondsUntilPortal(Player player, boolean beta)
	{
//		Party party = _partyManager.GetParty(player);
		long timeLeft = 0;

		if (_joinTime.containsKey(player.getName()))
		{
			timeLeft = (_joinTime.get(player.getName()) - System.currentTimeMillis()) + (beta ? BETA_PORTAL_TIMER : FREE_PORTAL_TIMER);
			
			if (timeLeft <= 0)
				timeLeft = 0;
		}

//		if (party != null)
//		{
//			if (player.getName().equals(party.GetLeader()))
//			{
//				for (Player partyPlayer : party.GetPlayersOnline())
//				{
//					if (!partyPlayer.equals(player))
//						timeLeft = Math.max(timeLeft, getMillisecondsUntilPortal(partyPlayer));
//				}
//			}
//		}

		return timeLeft;
	}
	
	public void RemoveServer(String serverName)
	{
		for (String key : _serverKeyInfoMap.keySet())
		{
			_serverKeyInfoMap.get(key).remove(serverName);
		}
		
		_serverInfoMap.remove(serverName);
	}
	
	public void addServerGroup(String serverKey, String...serverTag)
	{
		_serverKeyInfoMap.put(serverKey, new HashSet<ServerInfo>());
		
		for (String tag : serverTag)
			_serverKeyTagMap.put(tag, serverKey);
	}
	
	public void AddServerNpc(String serverNpcName, String...serverTag)
	{
		addServerGroup(serverNpcName, serverTag);
		_serverNpcShopMap.put(serverNpcName, new ServerNpcShop(this, _clientManager, _donationManager, serverNpcName));
	}
	
	public void RemoveServerNpc(String serverNpcName)
	{
		Set<ServerInfo> mappedServers = _serverKeyInfoMap.remove(serverNpcName);
		_serverNpcShopMap.remove(serverNpcName);
		
		if (mappedServers != null)
		{
			for (ServerInfo mappedServer : mappedServers)
			{
				boolean isMappedElseWhere = false;
				
				for (String key : _serverKeyInfoMap.keySet())
				{
					for (ServerInfo value : _serverKeyInfoMap.get(key))
					{
						if (value.Name.equalsIgnoreCase(mappedServer.Name))
						{
							isMappedElseWhere = true;
							break;
						}
					}
					
					if (isMappedElseWhere)
						break;
				}
				
				if (!isMappedElseWhere)
					_serverInfoMap.remove(mappedServer.Name);
			}
		}
	}
	
	public Collection<ServerInfo> GetServerList(String serverNpcName)
	{
		return _serverKeyInfoMap.get(serverNpcName);
	}
	
	public Set<String> GetAllServers()
	{
		return _serverInfoMap.keySet();
	}
	
	public ServerInfo GetServerInfo(String serverName)
	{
		return _serverInfoMap.get(serverName);
	}
	
	public boolean HasServerNpc(String serverNpcName) 
	{
		return _serverKeyInfoMap.containsKey(serverNpcName);
	}
	
	@EventHandler
	public void updatePages(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		_quickShop.UpdatePages();
		
		for (ServerNpcShop shop : _serverNpcShopMap.values())
		{
			shop.UpdatePages();
		}
	}
	
	@EventHandler
	public void updateServers(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC || (_retrieving && System.currentTimeMillis() - _lastRetrieve <= 5000))
			return;
		
		_alternateUpdateFire = !_alternateUpdateFire;
		
		if (!_alternateUpdateFire)
			return;
		
		_retrieving = true;
		
		
		_statusManager.retrieveServerGroups(new Callback<Collection<ServerGroup>>()
		{
			public void run(final Collection<ServerGroup> serverGroups)
			{
				final NautHashMap<String, ServerGroup> serverGroupMap = new NautHashMap<String, ServerGroup>();

				for (ServerGroup serverGroup : serverGroups)
				{
					serverGroupMap.put(serverGroup.getName(), serverGroup);
				}

				_statusManager.retrieveServerStatuses(new Callback<Collection<MinecraftServer>>()
				{
					public void run(Collection<MinecraftServer> serverStatusList)
					{
						_serverPlayerCounts.clear();

						for (MinecraftServer serverStatus : serverStatusList)
						{
							if (!_serverInfoMap.containsKey(serverStatus.getName()))
							{
								ServerInfo newServerInfo = new ServerInfo();
								newServerInfo.Name = serverStatus.getName();
								_serverInfoMap.put(serverStatus.getName(), newServerInfo);
							}

							String[] args = serverStatus.getMotd().split("\\|");
							String tag = (serverStatus.getName() != null && serverStatus.getName().contains("-")) ? serverStatus.getName().split("-")[0] : "N/A";

							//Private Servers
							if (serverGroupMap.containsKey(serverStatus.getGroup()))
							{
								ServerGroup serverGroup = serverGroupMap.get(serverStatus.getGroup());
								if (serverGroup.getHost() != null && !serverGroup.getHost().isEmpty())
									tag = "MPS";
							}

							ServerInfo serverInfo = _serverInfoMap.get(serverStatus.getName());
							serverInfo.MOTD = args.length > 0 ? args[0] : serverStatus.getMotd();
							serverInfo.CurrentPlayers = serverStatus.getPlayerCount();
							serverInfo.MaxPlayers = serverStatus.getMaxPlayerCount();
							serverInfo.HostedByStaff = serverStatus.getMotd().contains("StaffHosted");

							if (args.length > 1)
								serverInfo.ServerType = args[1];

							if (args.length > 2)
								serverInfo.Game = args[2];

							if (args.length > 3)
								serverInfo.Map = args[3];

							_serverUpdate.put(serverStatus.getName(), System.currentTimeMillis());

							if (_serverKeyTagMap.containsKey(tag))
							{
								_serverKeyInfoMap.get(_serverKeyTagMap.get(tag)).add(serverInfo);

								if (!_serverPlayerCounts.containsKey(tag))
									_serverPlayerCounts.put(tag, 0);

								_serverPlayerCounts.put(tag, _serverPlayerCounts.get(tag) + serverInfo.CurrentPlayers);
							}
						}

						for (String name : _serverUpdate.keySet())
						{
							if (_serverUpdate.get(name) != -1L && System.currentTimeMillis() - _serverUpdate.get(name) > 5000)
							{
								ServerInfo serverInfo = _serverInfoMap.get(name);
								serverInfo.MOTD = ChatColor.DARK_RED + "OFFLINE";
								serverInfo.CurrentPlayers = 0;
								serverInfo.MaxPlayers = 0;

								_serverUpdate.put(name, -1L);
							}
						}

						// Reset
						_retrieving = false;
						_lastRetrieve = System.currentTimeMillis();
					}
				});
			}
		});
	}



	public PartyManager getPartyManager()
	{
		return _partyManager;
	}
	
	public void SelectServer(org.bukkit.entity.Player player, ServerInfo serverInfo)
	{
		Party party = _partyManager.GetParty(player);
		
		if (party == null || player.getName().equals(party.GetLeader()))
		{
			player.leaveVehicle();
			player.eject();
			
			_portal.sendPlayerToServer(player, serverInfo.Name);
		}
	}

	public void ListServerNpcs(Player caller)
	{
		UtilPlayer.message(caller, F.main(getName(), "Listing Server Npcs:"));
		
		for (String serverNpc : _serverKeyInfoMap.keySet())
		{
			UtilPlayer.message(caller, F.main(getName(), C.cYellow + serverNpc));
		}
	}
	
	public void ListServers(Player caller, String serverNpcName)
	{
		UtilPlayer.message(caller, F.main(getName(), "Listing Servers for '" + serverNpcName + "':"));
		
		for (ServerInfo serverNpc : _serverKeyInfoMap.get(serverNpcName))
		{
			UtilPlayer.message(caller, F.main(getName(), C.cYellow + serverNpc.Name +  C.cWhite + " - " + serverNpc.MOTD + " " + serverNpc.CurrentPlayers + "/" + serverNpc.MaxPlayers));
		}
	}
	
	public void ListOfflineServers(Player caller)
	{
		UtilPlayer.message(caller, F.main(getName(), "Listing Offline Servers:"));
		
		for (ServerInfo serverNpc : _serverInfoMap.values())
		{
			if (serverNpc.MOTD.equalsIgnoreCase(ChatColor.DARK_RED + "OFFLINE"))
			{
				UtilPlayer.message(caller, F.main(getName(), C.cYellow + serverNpc.Name +  C.cWhite + " - " + F.time(UtilTime.convertString(System.currentTimeMillis() - _serverUpdate.get(serverNpc.Name), 0, TimeUnit.FIT))));
			}
		}
	}
	
	public void LoadServers()
	{
		_serverInfoMap.clear();
		_serverUpdate.clear();
		
		for (String npcName : _serverKeyInfoMap.keySet())
		{
			_serverKeyInfoMap.get(npcName).clear();
		}
		
		_serverKeyTagMap.clear();

		FileInputStream fstream = null;
		BufferedReader br = null;
		
		HashSet<String> npcNames = new HashSet<String>();
		
		try
		{
			File npcFile = new File("ServerManager.dat");

			if (npcFile.exists())
			{
				fstream = new FileInputStream(npcFile);
				br = new BufferedReader(new InputStreamReader(fstream));
				
				String line = br.readLine();
				
				while (line != null)
				{
					String serverNpcName = line.substring(0, line.indexOf('|')).trim();
					String[] serverTags = line.substring(line.indexOf('|') + 1, line.indexOf('|', line.indexOf('|') + 1)).trim().split(",");
					String[] locations = line.substring(line.indexOf('|', line.indexOf('|') + 1) + 1).trim().split(",");

					for (String location : locations)
					{
						_serverPortalLocations.put(ParseVector(location), serverNpcName);
					}
					
					if (!HasServerNpc(serverNpcName))
					{
						AddServerNpc(serverNpcName, serverTags);
					}
					
					npcNames.add(serverNpcName);
					
					line = br.readLine();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("ServerManager - Error parsing servers file : " + e.getMessage());
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			if (fstream != null)
			{
				try
				{
					fstream.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		for (String npcName : npcNames)
		{
			if (!_serverNpcShopMap.containsKey(npcName))
				_serverNpcShopMap.remove(npcName);
			
			if (!_serverKeyInfoMap.containsKey(npcName))
				_serverKeyInfoMap.remove(npcName);
		}
	}

	public int GetRequiredSlots(Player player, String serverType)
	{
		int slots = 0;
		
		Party party = _partyManager.GetParty(player);
		
		if (party != null)
		{
			if (player.getName().equals(party.GetLeader()))
			{
				for (String name : party.GetPlayers())
				{
					Player partyPlayer = UtilPlayer.searchExact(name);
					
					if (partyPlayer == null)
						continue;
					
					if (_clientManager.Get(partyPlayer).GetRank().Has(Rank.ULTRA) || _donationManager.Get(partyPlayer.getName()).OwnsUnknownPackage(serverType + " ULTRA"))
						continue;
					
					slots++;
				}
			}
		}
		else
		{
			if (!_clientManager.Get(player).GetRank().Has(Rank.ULTRA) && !_donationManager.Get(player.getName()).OwnsUnknownPackage(serverType + " ULTRA"))
				slots++;
		}
		
		return slots;
	}

	public ServerNpcShop getMixedArcadeShop()
	{
		return _serverNpcShopMap.get("Mixed Arcade");
	}

	public ServerNpcShop getSuperSmashMobsShop()
	{
		return _serverNpcShopMap.get("Super Smash Mobs");
	}

	@SuppressWarnings("rawtypes")
	public ShopBase getDominateShop()
	{
		return _serverNpcShopMap.get("Dominate");
	}

	public ServerNpcShop getBridgesShop()
	{
		return _serverNpcShopMap.get("The Bridges");
	}
	
	public ServerNpcShop getSurvivalGamesShop()
	{
		return _serverNpcShopMap.get("Survival Games");
	}

	public ServerNpcShop getBlockHuntShop()
	{
		return _serverNpcShopMap.get("Block Hunt");
	}

	public ServerNpcShop getBetaShop()
	{
		return _serverNpcShopMap.get("Beta Games");
	}

	public ServerNpcShop getUHCShop()
	{
		return _serverNpcShopMap.get("Ultra Hardcore");
	}

	public ServerNpcShop getSKYShop()
	{
		return _serverNpcShopMap.get("SkyWars");
	}

	public ServerNpcShop getMicroShop(){ return _serverNpcShopMap.get("MicroGames");}

	public ServerNpcShop getPlayerGamesShop()
	{
		return _serverNpcShopMap.get("Mineplex Player Servers");
	}
	
	private Vector ParseVector(String vectorString)
	{
		Vector vector = new Vector();
		
		String [] parts = vectorString.trim().split(" ");
		
		vector.setX(Double.parseDouble(parts[0]));
		vector.setY(Double.parseDouble(parts[1]));
		vector.setZ(Double.parseDouble(parts[2]));
		
		return vector;
	}

	public ServerStatusManager getStatusManager()
	{
		return _statusManager;
	}

	public ShopBase<ServerManager> getCastleSiegeShop()
	{
		return _serverNpcShopMap.get("Castle Siege");
	}

	public HubManager getHubManager()
	{
		return _hubManager;
	}

	public ShopBase<ServerManager> getDrawMyThingShop()
	{
		return _serverNpcShopMap.get("Draw My Thing");
	}

	public ShopBase<ServerManager> getTeamDeathmatchShop()
	{
		return _serverNpcShopMap.get("Team Deathmatch");
	}

	public ShopBase<ServerManager> getMinestrikeShop()
	{
		return _serverNpcShopMap.get("Mine-Strike");
	}

	public ShopBase<ServerManager> getWizardShop()
	{
		return _serverNpcShopMap.get("Wizards");
	}

	public int getGroupTagPlayerCount(String tag)
	{
		if (_serverPlayerCounts.containsKey(tag))
			return _serverPlayerCounts.get(tag);
		else
			return 0;
	}

	public ShopBase<ServerManager> getBuildShop()
	{
		return _serverNpcShopMap.get("Master Builders");
	}
}
