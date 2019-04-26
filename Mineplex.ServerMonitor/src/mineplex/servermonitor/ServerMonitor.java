package mineplex.servermonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import mineplex.core.common.util.NautHashMap;
import mineplex.serverdata.Region;
import mineplex.serverdata.commands.RestartCommand;
import mineplex.serverdata.commands.SuicideCommand;
import mineplex.serverdata.data.DedicatedServer;
import mineplex.serverdata.data.MinecraftServer;
import mineplex.serverdata.data.ServerGroup;
import mineplex.serverdata.servers.DedicatedServerSorter;
import mineplex.serverdata.servers.ServerManager;
import mineplex.serverdata.servers.ServerRepository;

public class ServerMonitor
{
	private static ServerRepository _repository = null;
	private static StatusHistoryRepository _historyRepository = null;
	private static int _count = 0;
	private static HashSet<ProcessRunner> _processes = new HashSet<ProcessRunner>();
	private static HashMap<String, Boolean> _badServers = new HashMap<String, Boolean>();
	private static Collection<MinecraftServer> _serverStatuses = null;
	private static Collection<ServerGroup> _serverGroups = null;
	private static Map<String, ServerGroup> _serverGroupMap = null;
	private static List<DedicatedServer> _dedicatedServers = null;
	private static HashSet<String> _deadServers = new HashSet<String>();
	private static HashSet<String> _laggyServers = new HashSet<String>();
	
	private static SimpleDateFormat _dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static Logger _logger = Logger.getLogger("ServerMonitor");
	
	private static int _totalPlayers = 0;
	private static Region _region;
	
	private static boolean _debug = false;

	public static void main (String args[])
	{
		/*
		MinecraftPingReply data = null;
		try
		{
			data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname("127.0.0.1").setPort(25565));
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		System.out.println(data.getDescription() + " " + data.getPlayers().getOnline());
		*/
		_region = !new File("eu.dat").exists() ? Region.US : Region.EU;
		_debug = new File("debug.dat").exists();
		_repository = ServerManager.getServerRepository(_region);	// Fetches and connects to server repo
		_historyRepository = new StatusHistoryRepository();
		File logFile = new File("monitor.log");
		
		if (!logFile.exists())
		{
			try
			{
				logFile.createNewFile();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		
		try
		{
			FileHandler fileHandler = new FileHandler("monitor.log", true);
			fileHandler.setFormatter(new CustomFormatter());
			_logger.addHandler(fileHandler);
			_logger.setUseParentHandlers(false);
		}
		catch (SecurityException | IOException e1)
		{
			e1.printStackTrace();
		}
		
		HashMap<String, Entry<String, Long>> serverTracker = new HashMap<String, Entry<String, Long>>();
		
		while (true)
		{
			try
			{
				_totalPlayers = 0;
				_serverStatuses = _repository.getServerStatuses();
				_serverGroups = _repository.getServerGroups(_serverStatuses);
				_serverGroupMap = new HashMap<String, ServerGroup>();
				_dedicatedServers = new ArrayList<DedicatedServer>(_repository.getDedicatedServers());
				
				calculateTotalPlayers();
				killDeadServers();
				
				double totalCPU = 0.0;
				double totalRAM = 0.0;
				double availableCPU = 0.0;
				double availableRAM = 0.0;
	
				for (DedicatedServer server : _dedicatedServers)
				{
					totalCPU += server.getAvailableCpu();
					totalRAM += server.getAvailableRam();
				}
				
				for (MinecraftServer minecraftServer : _serverStatuses)
				{
					if (minecraftServer.getMotd().contains("Finished") || (minecraftServer.getGroup().equalsIgnoreCase("UltraHardcore") && minecraftServer.getMotd().contains("Restarting") && minecraftServer.getPlayerCount() == 0))
					{
						killServer(minecraftServer.getName(), minecraftServer.getPublicAddress(), minecraftServer.getPlayerCount(), "[KILLED] [FINISHED] " + minecraftServer.getName() + ":" + minecraftServer.getPublicAddress(), true);
						
						handleUserServerGroup(_serverGroupMap.get(minecraftServer.getGroup()));
						continue;
					}
					
					for (DedicatedServer server : _dedicatedServers)
					{
						if (_serverGroupMap.containsKey(minecraftServer.getGroup()) && minecraftServer.getPublicAddress().equalsIgnoreCase(server.getPrivateAddress()))
						{
							ServerGroup serverGroup = _serverGroupMap.get(minecraftServer.getGroup());
							server.incrementServerCount(serverGroup);
						}
					}
				}
				
				if (_count % 15 == 0)
				{				
					_badServers.clear();
					
					for (DedicatedServer serverData : _dedicatedServers)
					{
						if (isServerOffline(serverData))
						{
							log("------=[OFFLINE]=------=[" + serverData.getName() + ":" + serverData.getPublicAddress() + "]=------=[OFFLINE]=------");
							_badServers.put(serverData.getName(), true);
						}
					}
					
					log(_badServers.size() + " bad servers.");
				}
				
				for (Iterator<DedicatedServer> iterator = _dedicatedServers.iterator(); iterator.hasNext();)
				{				
					DedicatedServer serverData = iterator.next();
				
					if (_badServers.containsKey(serverData.getName()))
						iterator.remove();
					else
					{
						availableCPU += serverData.getAvailableCpu();
						availableRAM += serverData.getAvailableRam();
					}
				}
				
				double usedCpuPercent = Math.round((1 - availableCPU / totalCPU) * 10000.0) / 100.0;
				double usedRamPercent = Math.round((1 - availableRAM / totalRAM) * 10000.0) / 100.0;
				
				log("Using " + usedCpuPercent +  "% of available CPU (" + availableCPU + " Free) and " + usedRamPercent + "% of available RAM (" + availableRAM / 1000 + "GB Free)");
				
				_historyRepository.saveDedicatedServerStats(_dedicatedServers);
				log("Saved Dedicated Server Stats.");
				_historyRepository.saveServerGroupStats((int)totalCPU, (int)totalRAM, _serverGroupMap.values());
				log("Saved ServerGroup Stats.");
				//_historyRepository.saveNetworkStats(usedCpuPercent, usedRamPercent, availableCPU, availableRAM, _region);
				log("Saved Network Stats.");
				
				for (ServerGroup groupStatus : _serverGroups)
				{
					NautHashMap<Integer, MinecraftServer> serverMap = new NautHashMap<Integer, MinecraftServer>();
					
					for (Iterator<MinecraftServer> serverIterator = groupStatus.getServers().iterator(); serverIterator.hasNext();)
					{
						try
						{
							MinecraftServer server = serverIterator.next();
							int serverNum = Integer.parseInt(server.getName().split("-")[1]);
							
							if (serverMap.containsKey(serverNum))
							{
								killServer(server.getName(), server.getPublicAddress(), server.getPlayerCount(), "[KILLED] [DUPLICATE] " + server.getName() + ":" + server.getPublicAddress(), true);
								serverIterator.remove();
							}
							else
							{
								serverMap.put(serverNum, server);
							}
						}
						catch (Exception exception)
						{
							exception.printStackTrace();
						}
					}
					
					/*
					if (groupStatus.getHost() == null || groupStatus.getHost().isEmpty())
					{
					if (groupStatus.getName().startsWith("0"))
					{
						int serverCount = groupStatus.getServers().size();
						log(groupStatus.getName() + " : " + groupStatus.getPlayerCount() + " players on " + serverCount + " servers " + String.format("%.2f", ((double)serverCount * (double)groupStatus.getRequiredCpu() / totalCPU)) + "% CPU," + String.format("%.2f", ((double)serverCount * (double)groupStatus.getRequiredRam() / totalRAM)) + "% RAM", false);
					}
					}
					*/
				}
				
				HashSet<String> onlineServers = new HashSet<String>();
				HashSet<String> laggyServers = new HashSet<String>();
				laggyServers.addAll(_laggyServers);
				_laggyServers.clear();
				
				for (MinecraftServer minecraftServer : _serverStatuses)
				{
					if (minecraftServer.getGroup().equalsIgnoreCase("Testing"))
						continue;
					
					onlineServers.add(minecraftServer.getName());
					
					if (minecraftServer.getTps() <= 17)
					{
						if (minecraftServer.getTps() <= 10)
						{
							if (laggyServers.contains(minecraftServer.getName()))
							{
								new RestartCommand(minecraftServer.getName(), _region).publish();
								log("[RESTART LAGGY] : " + minecraftServer.getName() + ":" + minecraftServer.getPublicAddress());
							}
							else
							{
								_laggyServers.add(minecraftServer.getName());
								log("[IMPENDING RESTART LAGGY] : " + minecraftServer.getName() + ":" + minecraftServer.getPublicAddress());
							}
						}
						else
							log("[Performance] " + minecraftServer.getName() + ":" + minecraftServer.getPublicAddress() + "] Running poorly at " + minecraftServer.getTps() + " TPS");
					}
				}
				
				for (Iterator<Entry<String, Entry<String, Long>>> iterator = serverTracker.entrySet().iterator(); iterator.hasNext();)
				{
					Entry<String, Entry<String, Long>> entry = iterator.next();
					
					if (onlineServers.contains(entry.getKey()))
						iterator.remove();
					else if (System.currentTimeMillis() - entry.getValue().getValue() > 35000)
					{	
						String serverName = entry.getKey();
						String serverAddress = entry.getValue().getKey();
						killServer(serverName, serverAddress, 0, "[KILLED] [SLOW-STARTUP] " + serverName + ":" + serverAddress, true);
						iterator.remove();
					}
				}
				
				for (ServerGroup serverGroup : _serverGroups)
				{
					if (serverGroup.getName().equals("Testing"))
						continue;
					
					try
					{
						handleGroupChanges(serverTracker, serverGroup, false);
					}
					catch (Exception exception)
					{
						log(exception.getMessage());
						log("Can't handle group changes for " + serverGroup.getName());
					}
				}
			
				int processWaits = 0;
				
				while (_processes.size() > 0)
				{
					for (Iterator<ProcessRunner> iterator = _processes.iterator(); iterator.hasNext();)
					{
						ProcessRunner pr = iterator.next();
						
						try 
						{
							pr.join(100);
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						
						if (pr.isDone())
							iterator.remove();
					}
					
					if (_processes.size() > 0)
					{
						try
						{
							log("Sleeping while processes run...");
							Thread.sleep(6000);
						} 
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					
					if (processWaits >= 10)
					{
						log("Killing stale processes.");
						
						for (Iterator<ProcessRunner> iterator = _processes.iterator(); iterator.hasNext();)
						{
							iterator.next().abort();
							iterator.remove();
						}
					}
					
					processWaits++;
				}
			
				processWaits = 0;
				
				try
				{
					log("Natural sleep.");
					Thread.sleep(10000);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
				_count++;
			}
			catch (Exception ex)
			{
				log("Error doing something : " + ex.getMessage());
				
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static void killDeadServers()
	{
		HashSet<String> deadServers = new HashSet<String>();
		deadServers.addAll(_deadServers);
		
		_deadServers.clear();
		for (MinecraftServer deadServer : _repository.getDeadServers())
		{
			if (deadServer.getUptime() <= 10 || deadServer.getGroup().equalsIgnoreCase("Testing"))
				continue;
			
			if (_count == 0 || deadServers.contains(deadServer.getName()))
			{
				killServer(deadServer.getName(), deadServer.getPublicAddress(), deadServer.getPlayerCount(), "[KILLED] [DEAD] " + deadServer.getName() + ":" + deadServer.getPublicAddress(), true);
				
				handleUserServerGroup(_serverGroupMap.get(deadServer.getGroup()));
			}
			else
			{
				_deadServers.add(deadServer.getName());
				log("[IMPENDING DEATH] : " + deadServer.getName() + ":" + deadServer.getPublicAddress());
			}
		}
	}

	private static void handleUserServerGroup(ServerGroup serverGroup)
	{
		if (serverGroup != null && serverGroup.getHost() != null && !serverGroup.getHost().isEmpty() && serverGroup.getServerCount() <= 1)
		{
			_repository.removeServerGroup(serverGroup);
			_serverGroupMap.remove(serverGroup);
			_serverGroups.remove(serverGroup);
			System.out.println("Removed ServerGroup : " + serverGroup.getName());
		}
	}
	
	private static void calculateTotalPlayers()
	{
		for (ServerGroup serverGroup : _serverGroups)
		{
			_serverGroupMap.put(serverGroup.getName(), serverGroup);
			_totalPlayers += serverGroup.getPlayerCount();
		}
		
		log("Total Players : " + _totalPlayers);
	}

	private static void handleGroupChanges(HashMap<String, Entry<String, Long>> serverTracker, ServerGroup serverGroup, boolean free)
	{
		int serverNum = 0;
		int requiredTotal = serverGroup.getRequiredTotalServers();
		int requiredJoinable = serverGroup.getRequiredJoinableServers();
		int joinableServers = serverGroup.getJoinableCount();
		int totalServers = serverGroup.getServerCount();
		int serversToAdd = Math.max(0, Math.max(requiredTotal - totalServers, requiredJoinable - joinableServers));
		int serversToKill = (totalServers > requiredTotal && joinableServers > requiredJoinable) ? Math.min(joinableServers - requiredJoinable, serverGroup.getEmptyServers().size()) : 0;
		int serversToRestart = 0;

		// Minimum 1500 slot bufferzone
		if (serverGroup.getName().equalsIgnoreCase("Lobby"))
		{
			int availableSlots = serverGroup.getMaxPlayerCount() - serverGroup.getPlayerCount();
			
			if (availableSlots < 1500)
			{
				serversToAdd = Math.max(1, (1500 - availableSlots) / serverGroup.getMaxPlayers());
				serversToAdd = Math.min(250 - totalServers, serversToAdd);
				serversToKill = 0;
			}
			else if (serversToKill > 0)
				serversToKill = Math.min(serversToKill, (availableSlots - 1500) / 80);
			else if (serversToAdd == 0 && joinableServers > requiredJoinable && totalServers > requiredTotal)
			{
				serversToRestart = Math.min(joinableServers - requiredJoinable, joinableServers - requiredTotal);
				serversToRestart = Math.min(serversToRestart, (availableSlots - 1500) / 80);
				
				if (serversToRestart <= 5)
					serversToRestart = 0;
			}
			
			
		}
		else if (serverGroup.getName().equalsIgnoreCase("Halloween"))
		{
			if (serverGroup.getServers().size() > (_region == Region.US ? 300 : 100))
			{
				serversToAdd = 0;
			}
		}
		else if (serverGroup.getName().equalsIgnoreCase("Christmas"))
		{
			if (serverGroup.getServers().size() > (_region == Region.US ? 300 : 100))
			{
				serversToAdd = 0;
			}
		}
		else if (serverGroup.getName().equalsIgnoreCase("UltraHardcore"))
		{
			int maxUHC = Math.max(1, _totalPlayers / 6000);
			
			if (serversToAdd > 0)
				serversToAdd = maxUHC - joinableServers;
			
			if (joinableServers > maxUHC)
				serversToKill = maxUHC - joinableServers;
		}
		else if (serverGroup.getName().equalsIgnoreCase("Testing"))
		{
			return;
		}
		
		// KILL, CLEAN, THEN ADD
		while (serversToKill > 0)
		{
			List<MinecraftServer> emptyServers = new ArrayList<MinecraftServer>(serverGroup.getEmptyServers());
			MinecraftServer emptyServer = emptyServers.get(serversToKill - 1);
			killServer(emptyServer, "[KILLED] [EXCESS] " + emptyServer.getName() + ":" + emptyServer.getPublicAddress());
		    serversToKill--;
		}
		
		while (serversToAdd > 0)
		{
			serverNum = serverGroup.generateUniqueId(serverNum + 1);
			
			while (_deadServers.contains(serverGroup.getPrefix() + "-" + serverNum))
			{
				serverNum = serverGroup.generateUniqueId(serverNum + 1);
			}
			
			Collections.sort(_dedicatedServers, new DedicatedServerSorter());
			DedicatedServer bestServer = getBestDedicatedServer(_dedicatedServers, serverGroup);

			if (bestServer == null)
			{
				log("!!!!!!!!!!!!!!!!!!!!!!!!!!!! NO DEDICATED SERVER AVAILABLE FOR GROUP " + serverGroup.getName() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
			}

			if (serverTracker.containsKey(serverGroup.getPrefix() + "-" + serverNum))
				log("[WAITING] On " + serverGroup.getPrefix() + "-" + serverNum + " to finish starting...");
			else
			{
				startServer(bestServer, serverGroup, serverNum, free);
				serverTracker.put(serverGroup.getPrefix() + "-" + serverNum, new AbstractMap.SimpleEntry<String, Long>(bestServer.getPublicAddress(), System.currentTimeMillis()));
			}
			
		    serversToAdd--;
		}
		
		List<MinecraftServer> servers = new ArrayList<MinecraftServer>();
		servers.addAll(serverGroup.getServers());
		Collections.sort(servers, new ServerSorter());
		
		while (serversToRestart > 0)
		{
			MinecraftServer server = servers.get(servers.size() - serversToRestart);
			new SuicideCommand(server.getName(), _region).publish();
			log("[RESTART/KILL EXCESS] : " + server.getName() + ":" + server.getPublicAddress() + " " + server.getPlayerCount() + " players");
			serversToRestart--;
		}
	}

	private static void killServer(final String serverName, final String serverAddress, final int players, final String message, final boolean announce)
	{
		if (_debug)
			return;
		
		String cmd = "/home/mineplex/easyRemoteKillServer.sh";
		
		ProcessRunner pr = new ProcessRunner(new String[] {"/bin/sh", cmd, serverAddress, serverName});
		pr.start(new GenericRunnable<Boolean>()		
		{
			public void run(Boolean error)
			{
				MinecraftServer server = null;
				
				if (!error)
				{
					server = _repository.getServerStatus(serverName);
					
					if (server != null)
					{
						_repository.removeServerStatus(server);
					}
				}
				
				if (announce)
				{
					if (error)
						log("[" + serverName + ":" + serverAddress + "] Kill errored.");
					else
						log(message + " Players: " + players);
				}
			}
		});
		
		try 
		{
			pr.join(50);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		
		
		if (!pr.isDone())
			_processes.add(pr);
	}

	private static boolean isServerOffline(DedicatedServer serverData)
	{
		boolean success = false;
		
		if (_debug)
			return false;
		
		Process process = null;
		String cmd = "/home/mineplex/isServerOnline.sh";
		
		ProcessBuilder processBuilder = new ProcessBuilder(new String[] {"/bin/sh", cmd, serverData.getPublicAddress()});
	
		try
		{
			process = processBuilder.start();
			process.waitFor();
			
            BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream())); 
            String line = reader.readLine(); 
            
            while(line != null) 
            {
            	success = line.equals("Success");
            	
	            line=reader.readLine(); 
            } 
        } 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		finally
		{
			process.destroy();
		}

		return !success;
	}

	private static DedicatedServer getBestDedicatedServer(Collection<DedicatedServer> dedicatedServers,	ServerGroup serverGroup)
	{
		DedicatedServer bestServer = null;
		
		for (DedicatedServer serverData : dedicatedServers)
		{
			if (serverData.getAvailableRam() > serverGroup.getRequiredRam() 
					&& serverData.getAvailableCpu() > serverGroup.getRequiredCpu())
			{
				if (bestServer == null || serverData.getServerCount(serverGroup) < bestServer.getServerCount(serverGroup))
				{
					bestServer = serverData;
				}
			}
		}
		
		return bestServer;
	}

	private static void killServer(final MinecraftServer serverToKill, String message)
	{
		killServer(serverToKill.getName(), serverToKill.getPublicAddress(), serverToKill.getPlayerCount(), message, true);
	}
	
	private static void startServer(final DedicatedServer serverSpace, final ServerGroup serverGroup, final int serverNum, final boolean free)
	{
		if (_debug)
			return;
		
		String cmd = "/home/mineplex/easyRemoteStartServerCustom.sh";
		final String groupPrefix = serverGroup.getPrefix();
		final String serverName = serverSpace.getName();
		final String serverAddress = serverSpace.getPublicAddress();

		ProcessRunner pr = new ProcessRunner(new String[] {"/bin/sh", cmd, serverAddress, serverSpace.getPrivateAddress(), (serverGroup.getPortSection() + serverNum) + "", serverGroup.getRequiredRam() + "", serverGroup.getWorldZip(), serverGroup.getPlugin(), serverGroup.getConfigPath(), serverGroup.getName(), serverGroup.getPrefix() + "-" + serverNum, serverSpace.isUsRegion() ? "true" : "false", serverGroup.getAddNoCheat() + "", serverGroup.getAddWorldEdit() + "" });
		pr.start(new GenericRunnable<Boolean>()		
		{
			public void run(Boolean error)
			{
				if (error)
					log("[" + serverName + ":" + serverAddress + " Free Resources; CPU " + serverSpace.getAvailableCpu() + " RAM " + serverSpace.getAvailableRam() + "MB] Errored " + serverName + "(" + groupPrefix+ "-" + serverNum + (free ? "-FREE" : "") + ")");
				else
					log("[" + serverName + ":" + serverAddress + " Free Resources; CPU " + serverSpace.getAvailableCpu() + " RAM " + serverSpace.getAvailableRam() + "MB] Added " + serverName + "(" + groupPrefix+ "-" + serverNum + (free ? "-FREE" : "") + ")");

			}
		});
		
		try 
		{
			pr.join(100);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		
		serverSpace.incrementServerCount(serverGroup);
		
		if (!pr.isDone())
			_processes.add(pr);
	}
	
	private static void log(String message)
	{
		log(message, false);
	}
	
	private static void log(String message, boolean fileOnly)
	{
		_logger.info("[" + _dateFormat.format(new Date()) + "] " + message);
		
		if (!fileOnly)
			System.out.println("[" + _dateFormat.format(new Date()) + "] " + message);
	}
}
