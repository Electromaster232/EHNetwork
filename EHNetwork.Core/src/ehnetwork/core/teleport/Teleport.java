package ehnetwork.core.teleport;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.event.ClientUnloadEvent;
import ehnetwork.core.common.jsonchat.ChildJsonMessage;
import ehnetwork.core.common.jsonchat.ClickEvent;
import ehnetwork.core.common.jsonchat.HoverEvent;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.teleport.command.LocateCommand;
import ehnetwork.core.teleport.command.TeleportCommand;
import ehnetwork.core.teleport.event.MineplexTeleportEvent;
import ehnetwork.core.teleport.redis.RedisLocate;
import ehnetwork.core.teleport.redis.RedisLocateCallback;
import ehnetwork.core.teleport.redis.RedisLocateHandler;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.serverdata.commands.ServerCommandManager;

public class Teleport extends MiniPlugin
{
	private LinkedList<Teleporter> teleportList = new LinkedList<Teleporter>();
	private NautHashMap<String, LinkedList<Location>> _tpHistory = new NautHashMap<String, LinkedList<Location>>();
	private NautHashMap<UUID, BukkitRunnable> _failedRedisLocates = new NautHashMap<UUID, BukkitRunnable>();
	private String _serverName;

	public Teleport(JavaPlugin plugin) 
	{
		super("Teleport", plugin);
		
		_serverName = getPlugin().getConfig().getString("serverstatus.name");

		RedisLocateHandler locateHandler = new RedisLocateHandler(this);

		ServerCommandManager.getInstance().registerCommandType("RedisLocate", RedisLocate.class, locateHandler);
		ServerCommandManager.getInstance().registerCommandType("RedisLocateCallback", RedisLocateCallback.class, locateHandler);
	}

	@Override
	public void addCommands()
	{
		addCommand(new TeleportCommand(this));
		addCommand(new LocateCommand(this));
	}
	
	public void handleLocateCallback(RedisLocateCallback callback)
	{
		BukkitRunnable runnable = _failedRedisLocates.remove(callback.getUUID());
		
		if (runnable != null)
		{
			runnable.cancel();
		}
		
		Player player = Bukkit.getPlayerExact(callback.getReceivingPlayer());
	
		if (player != null)
		{
			ChildJsonMessage message = new JsonMessage("").extra(C.mHead + "Locate" + "> " + C.mBody + "Located [" + C.mElem
					+ callback.getLocatedPlayer() + C.mBody + "] at ");
			
			message.add(C.cBlue + callback.getServer()).click(ClickEvent.RUN_COMMAND,
					"/server " + callback.getServer());
			
			message.hover(HoverEvent.SHOW_TEXT, "Teleport to " + callback.getServer());

			message.sendToPlayer(player);
		}
	}
	
	public void locatePlayer(final Player player, final String target)
	{
		Player targetPlayer = Bukkit.getPlayerExact(target);
		
		if (targetPlayer != null)
		{
			UtilPlayer.message(player, F.main("Locate", C.mBody + " [" + C.mElem + target + C.mBody + "] is in the same server!"));
			return;
		}
		
		RedisLocate locate = new RedisLocate(_serverName, player.getName(), target);
		final UUID uuid = locate.getUUID();
		
		BukkitRunnable runnable = new BukkitRunnable()
		{

			@Override
			public void run()
			{
				_failedRedisLocates.remove(uuid);
				UtilPlayer.message(player, F.main("Locate", C.mBody + "Failed to locate [" + C.mElem + target + C.mBody + "]."));
			}
			
		};
		
		_failedRedisLocates.put(uuid, runnable);
		runnable.runTaskLater(_plugin, 40);
		
		locate.publish();
	}

	@EventHandler
	public void UnloadHistory(ClientUnloadEvent event)
	{
		_tpHistory.remove(event.GetName());
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return; 
		
		if (teleportList.isEmpty())
			return;

		teleportList.removeFirst().doTeleport();
	}

	public void playerToPlayer(Player caller, String from, String to)
	{
		LinkedList<Player> listA = new LinkedList<Player>();

		//ALL
		if (from.equals("%ALL%"))
			for (Player cur : UtilServer.getPlayers())
				listA.add(cur);
		//Normal
		else
			listA = UtilPlayer.matchOnline(caller, from, true);

		//To
		Player pB = UtilPlayer.searchOnline(caller, to, true);

		if (listA.isEmpty() || pB == null)
			return;

		if (listA.size() == 1)
		{
			Player pA = listA.getFirst();

			String mA = null;
			String mB = null;

			//Inform
			if (pA.equals(caller))
			{
				mA = F.main("Teleport", "You teleported to " + F.elem(pB.getName()) + ".");
			}
			else if (pB.equals(caller))
			{
				mA = F.main("Teleport", F.elem(caller.getName()) + " teleported you to themself.");
				mB = F.main("Teleport", "You teleported " + F.elem(pA.getName()) + " to yourself.");
			}	
			else
			{
				mA = F.main("Teleport", F.elem(caller.getName()) + " teleported you to " + F.elem(pB.getName()) + ".");
				mB = F.main("Teleport", "You teleported " + F.elem(pA.getName()) + " to " + F.elem(pB.getName()) + ".");
			}

			//Register
			Add(pA, pB.getLocation(), mA, true, caller, mB,
					pA.getName() + " teleported to " + pB.getName() + " via " + caller.getName());
			return;
		}

		boolean first = true;
		for (Player pA : listA)
		{
			String mA = null;
			String mB = null;

			//Inform
			if (pA.equals(caller))
			{
				mA = F.main("Teleport", "You teleported to " + F.elem(pB.getName()) + ".");
			}
			else if (pB.equals(caller))
			{
				mA = F.main("Teleport", F.elem(caller.getName()) + " teleported you to themself.");
				mB = F.main("Teleport", "You teleported " + F.elem(listA.size() + " Players") + " to yourself.");
			}	
			else
			{
				mA = F.main("Teleport", F.elem(caller.getName()) + " teleported you to " + F.elem(pB.getName()) + ".");
				mB = F.main("Teleport", "You teleported " + F.elem(listA.size() + " Players") + " to " + F.elem(pB.getName()) + ".");
			}

			//Register
			if (first)		
				Add(pA, pB.getLocation(), mA, true, caller, mB,	pA.getName() + " teleported to " + pB.getName() + " via " + caller.getName());
			
			else
				Add(pA, pB.getLocation(), mA, true, caller, null, pA.getName() + " teleported to " + pB.getName() + " via " + caller.getName());
			
			first = false;
		}
	}

	public void playerToLoc(Player caller, String target, String sX, String sY, String sZ) 
	{
		playerToLoc(caller, target, caller.getWorld().getName(), sX, sY, sZ);
	}
	
	public void playerToLoc(Player caller, String target, String world, String sX, String sY, String sZ) 
	{
		Player player = UtilPlayer.searchOnline(caller, target, true);

		if (player == null)
			return;

		try
		{
			int x = sX.matches(".*[0-9]") ? Integer.parseInt(sX.replace("~", "")) : 0;
			int y = sY.matches(".*[0-9]") ? Integer.parseInt(sY.replace("~", "")) : 0;
			int z = sZ.matches(".*[0-9]") ? Integer.parseInt(sZ.replace("~", "")) : 0;

			Location pLoc = player.getLocation();

			if (sX.startsWith("~"))
				x += pLoc.getBlockX();

			if (sY.startsWith("~"))
				y += pLoc.getBlockY();

			if (sZ.startsWith("~"))
				z += pLoc.getBlockZ();

			Location loc = new Location(Bukkit.getWorld(world), x, y, z);

			//Inform
			String mA = null;
			if (caller == player)	mA = F.main("Teleport", "You teleported to " + UtilWorld.locToStrClean(loc) + ".");
			else					mA = F.main("Teleport", F.elem(caller.getName()) + " teleported you to " + UtilWorld.locToStrClean(loc) + ".");

			//Register
			Add(player, loc, mA, true, caller, null, player.getName() + " teleported to " + UtilWorld.locToStrClean(loc) + " via " + caller.getName());
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Teleport", "Invalid Location [" + sX + "," + sY + "," + sZ + "]."));
			return;
		}
	}

	public void Add(Player pA, Location loc, String mA, boolean record, Player pB, String mB, String log)
	{
		teleportList.addLast(new Teleporter(this, pA, pB, mA, mB, loc, record, log));
	}
	
	public void TP(Player player, Location getLocation)
	{
		TP(player, getLocation, true);
	}
	
	public void TP(Player player, Location loc, boolean dettach) 
	{
		//Event
		MineplexTeleportEvent event = new MineplexTeleportEvent(player, loc);
		UtilServer.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		if (dettach)
		{
			player.eject();
			player.leaveVehicle();
		}
		
		player.setFallDistance(0);
		player.setVelocity(new Vector(0,0,0));
		
		player.teleport(loc);
	}
	
	public LinkedList<Location> GetTPHistory(Player player)
	{
		return _tpHistory.get(player.getName());
	}
}
