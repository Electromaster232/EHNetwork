package ehnetwork.core.antihack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.antihack.types.Fly;
import ehnetwork.core.antihack.types.Idle;
import ehnetwork.core.antihack.types.Reach;
import ehnetwork.core.antihack.types.Speed;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.portal.Portal;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.punish.Punish;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class AntiHack extends MiniPlugin
{
	public static AntiHack Instance;

	private boolean _enabled = true;
	private boolean _strict = false;
	private boolean _kick = true;

	public ehnetwork.core.punish.Punish Punish;
	public ehnetwork.core.portal.Portal Portal;
	private PreferencesManager _preferences;
	private CoreClientManager _clientManager;

	//Record Offenses
	private HashMap<Player, HashMap<String, ArrayList<Long>>> _offense = new HashMap<Player, HashMap<String, ArrayList<Long>>>();

	//Ignore Player
	private HashMap<Player, Long> _ignore = new HashMap<Player, Long>();

	//Player Info
	private HashSet<Player> _velocityEvent = new HashSet<Player>();
	private HashMap<Player, Long> _lastMoveEvent = new HashMap<Player, Long>();	
	
	private HashSet<Player> _hubAttempted = new HashSet<Player>();

	//Hack Requirements
	public int FloatHackTicks = 10; 
	public int HoverHackTicks = 4; 
	public int RiseHackTicks = 6;
	public int SpeedHackTicks = 6;
	public int IdleTime = 20000; 

	public int KeepOffensesFor = 30000;

	//Other Times
	public int FlightTriggerCancel = 2000;

	public ArrayList<Detector> _movementDetectors;
	public ArrayList<Detector> _combatDetectors;
	
	private AntiHackRepository _repository;

	protected AntiHack(JavaPlugin plugin, Punish punish, Portal portal, PreferencesManager preferences, CoreClientManager clientManager)
	{
		super("AntiHack", plugin);

		Punish = punish;
		Portal = portal;
		_preferences = preferences;
		_clientManager = clientManager;

		_repository = new AntiHackRepository(plugin.getConfig().getString("serverstatus.name"));
		_repository.initialize();

		_movementDetectors = new ArrayList<Detector>();
		_combatDetectors = new ArrayList<Detector>();

		_movementDetectors.add(new Fly(this));
		_movementDetectors.add(new Idle(this));
		_movementDetectors.add(new Speed(this));
		
		_combatDetectors.add(new Reach(this));
	}

	public static void Initialize(JavaPlugin plugin, Punish punish, Portal portal, PreferencesManager preferences, CoreClientManager clientManager)
	{
		Instance = new AntiHack(plugin, punish, portal, preferences, clientManager);
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent event)
	{
		if (!_enabled)
			return;

		_lastMoveEvent.put(event.getPlayer(), System.currentTimeMillis());
	}

	@EventHandler
	public void playerTeleport(PlayerTeleportEvent event)
	{
		if (!_enabled)
			return;

		setIgnore(event.getPlayer(), 2000);
	}

	@EventHandler
	public void playerVelocity(PlayerVelocityEvent event)
	{
		if (!_enabled)
			return;

		_velocityEvent.add(event.getPlayer());
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		if (!_enabled)
			return;

		resetAll(event.getPlayer());
	}

	@EventHandler
	public void startIgnore(PlayerMoveEvent event)
	{
		if (!_enabled)
			return;

		Player player = event.getPlayer();

		if (_velocityEvent.remove(player))
		{
			setIgnore(player, 2000);
		}

		//Initial Move (or Lag) Ignore
		if (_lastMoveEvent.containsKey(player))
		{
			long timeBetweenPackets = System.currentTimeMillis() - _lastMoveEvent.get(player);

			if (timeBetweenPackets > 500)
			{
				setIgnore(player, Math.min(4000, timeBetweenPackets));
			}
		}
	}

	public void setIgnore(Player player, long time)
	{
		//Wipe Detection
		for (Detector detector : _movementDetectors)
			detector.Reset(player);

		//Already ignoring for a longer period
		if (_ignore.containsKey(player) && _ignore.get(player) > System.currentTimeMillis() + time)
			return;

		//Add Ignore
		_ignore.put(player, System.currentTimeMillis() + time);
	}

	public boolean isValid(Player player, boolean groundValid)
	{
		//Near Other Player
		for (Player other : UtilServer.getPlayers())
		{
			if (player.equals(other))
				continue;
			
			if (other.getGameMode() != GameMode.SURVIVAL || UtilPlayer.isSpectator(player))
				continue;
			
			if (other.getVehicle() != null)
				continue;
			
			if (UtilMath.offset(player, other) < 2)
				return true;
		}
		
		if (player.isFlying() || player.isInsideVehicle() || player.getGameMode() != GameMode.SURVIVAL || UtilPlayer.isSpectator(player))
		{
			return true;
		}

		//On Ground
		if (groundValid)
		{
			if (UtilEnt.onBlock(player) || player.getLocation().getBlock().getType() != Material.AIR)
			{
				return true;
			}
		}

		if ((_ignore.containsKey(player) && System.currentTimeMillis() < _ignore.get(player)))
		{
			return true;
		}

		return false;
	}

	public void addSuspicion(Player player, String type)
	{
		if (!_enabled)
			return;

		System.out.println(C.cRed + C.Bold + player.getName() + " suspected for " + type + ".");

		//Add Offense
		if (!_offense.containsKey(player))
			_offense.put(player, new HashMap<String, ArrayList<Long>>());

		if (!_offense.get(player).containsKey(type))
			_offense.get(player).put(type, new ArrayList<Long>());

		_offense.get(player).get(type).add(System.currentTimeMillis());

		//Cull & Count
		int total = 0;
		for (String curType : _offense.get(player).keySet())
		{
			//Remove Old Offenses
			Iterator<Long> offenseIterator = _offense.get(player).get(curType).iterator();
			while (offenseIterator.hasNext())
			{
				if (UtilTime.elapsed(offenseIterator.next(), KeepOffensesFor))
					offenseIterator.remove();
			}

			//Count
			total += _offense.get(player).get(curType).size();
		}


		//Inform
		for (Player admin : UtilServer.getPlayers())
			if (_clientManager.Get(admin).GetRank().Has(Rank.MODERATOR) && _preferences.Get(admin).ShowMacReports)
			{
				UtilPlayer.message(admin, "#" + total + ": " + C.cRed + C.Bold + player.getName() + " suspected for " + type + ".");
			}

		// Print (Debug)
		System.out.println("[Offense] #" + total + ": "+ player.getName() + " received suspicion for " + type + ".");
	}

	@EventHandler
	public void generateReports(UpdateEvent event)
	{
		if (!_enabled)
			return;

		if (event.getType() != UpdateType.SEC)
			return;

		for (Iterator<Entry<Player, HashMap<String, ArrayList<Long>>>> playerIterator = _offense.entrySet().iterator(); playerIterator.hasNext();)
		{
			Entry<Player, HashMap<String, ArrayList<Long>>> entry = playerIterator.next();
			Player player = entry.getKey();
			
			String out = "";
			int total = 0;

			for (String type : entry.getValue().keySet())
			{
				//Remove Old Offenses
				Iterator<Long> offenseIterator = entry.getValue().get(type).iterator();
				while (offenseIterator.hasNext())
				{
					long time = offenseIterator.next();

					if (UtilTime.elapsed(time, KeepOffensesFor))
						offenseIterator.remove();
				}

				//Count
				int count = entry.getValue().get(type).size();
				total += count;

				out += count + " " + type + ", ";
			}

			if (out.length() > 0)
				out = out.substring(0, out.length() - 2);

			String severity = "Low";
			
			if (total > (_strict ? 6 : 18))
				severity = "Extreme";
			else if (total > (_strict ? 4 : 12))
				severity = "High";
			else if (total > (_strict ? 2 : 6))
				severity = "Medium";

			//Send Report
			sendReport(player, out, severity);
			
			if (severity.equalsIgnoreCase("Extreme"))
			{
				playerIterator.remove();
				resetAll(player, false);
			}
		}
	}

	public void sendReport(Player player, String report, String severity)
	{
		if (severity.equals("Extreme"))
		{
			//Staff
			boolean handled = false;
			for (Player staff : UtilServer.getPlayers())
			{
				if (_clientManager.Get(staff).GetRank().Has(Rank.MODERATOR))
				{
					UtilPlayer.message(staff, C.cAqua + C.Scramble + "A" + ChatColor.RESET + C.cRed + C.Bold + " MAC > " + ChatColor.RESET + C.cYellow + report);
					UtilPlayer.message(staff, C.cAqua + C.Scramble + "A" + ChatColor.RESET + C.cRed + C.Bold + " MAC > " + ChatColor.RESET + C.cGold + player.getName() + C.cYellow + " has extreme violation. Please investigate.");

					handled = true;
				}
			}

			//Auto-Kick
			if (!handled && _clientManager.Get(player).GetRank() != Rank.YOUTUBE && _clientManager.Get(player).GetRank() != Rank.TWITCH)
			{
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 2f, 0.5f);

				if (_kick || _hubAttempted.remove(player))
				{
					player.kickPlayer(
							C.cGold + "Mineplex Anti-Cheat" + "\n" + 
									C.cWhite + "You were kicked for suspicious movement." + "\n" + 
									C.cWhite + "Cheating may result in a " + C.cRed + "Permanent Ban" + C.cWhite + "." + "\n" + 
									C.cWhite + "If you were not cheating, you will not be banned."
							);
				}
				else
				{
					_hubAttempted.add(player);
					
					UtilPlayer.message(player,  C.cGold + C.Strike + "---------------------------------------------");
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, C.cGold + "Mineplex Anti-Cheat");
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, "You were kicked from the game for suspicious movement.");
					UtilPlayer.message(player, "Cheating may result in a " + C.cRed + "Permanent Ban" + C.cWhite + ".");
					UtilPlayer.message(player, "If you were not cheating, you will not be banned.");
					UtilPlayer.message(player, "");
					UtilPlayer.message(player,  C.cGold + C.Strike + "---------------------------------------------");
					Portal.sendPlayerToServer(player, "Lobby");	
				}

				UtilServer.broadcast(F.main("MAC", player.getName() + " was kicked for suspicious movement."));
			}

			//Record
			ServerListPingEvent event = new ServerListPingEvent(null, Bukkit.getServer().getMotd(), Bukkit.getServer().getOnlinePlayers().size(), Bukkit.getServer().getMaxPlayers());
			getPluginManager().callEvent(event);

			String motd = event.getMotd();
			String game = "N/A";
			String map = "N/A";

			String[] args = motd.split("\\|");

			if (args.length > 0)
				motd = args[0];

			if (args.length > 2)
				game = args[2];

			if (args.length > 3)
				map = args[3];

			_repository.saveOffense(player, motd, game, map, report);
		}
	}

	private void reset()
	{
		for (Player player : UtilServer.getPlayers())
			resetAll(player);
	}

	private void resetAll(Player player)
	{
		resetAll(player, true);
	}
	
	private void resetAll(Player player, boolean removeOffenses)
	{
		_ignore.remove(player);
		_velocityEvent.remove(player);
		_lastMoveEvent.remove(player);

		if (removeOffenses)
			_offense.remove(player);

		for (Detector detector : _movementDetectors)
			detector.Reset(player);
		
		for (Detector detector : _combatDetectors)
			detector.Reset(player);
	}

	@EventHandler
	public void cleanupPlayers(UpdateEvent event)
	{
		if (!_enabled)
			return;

		if (event.getType() != UpdateType.SLOW)
			return;

		for (Iterator<Entry<Player, Long>> playerIterator = _ignore.entrySet().iterator(); playerIterator.hasNext();)
		{
			Player player = playerIterator.next().getKey();

			if (!player.isOnline() || player.isDead() || !player.isValid())
			{	
				playerIterator.remove();

				_velocityEvent.remove(player);
				_lastMoveEvent.remove(player);

				_offense.remove(player);

				for (Detector detector : _movementDetectors)
					detector.Reset(player);
				
				for (Detector detector : _combatDetectors)
					detector.Reset(player);
			}
		}
		

		for (Iterator<Player> playerIterator = _hubAttempted.iterator(); playerIterator.hasNext();)
		{
			Player player = playerIterator.next();

			if (!player.isOnline() || !player.isValid())
			{	
				playerIterator.remove();
			}
		}
	}

	public void setEnabled(boolean b)
	{
		_enabled = b;
		System.out.println("MAC Enabled: " + b);
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public void setStrict(boolean strict)
	{
		_strict = strict;

		reset();

		System.out.println("MAC Strict: " + strict);
	}

	public boolean isStrict()
	{
		return _strict;
	}
	
	public void setKick(boolean kick)
	{
		_kick = kick;

		System.out.println("MAC Kick: " + kick);
	}
}
