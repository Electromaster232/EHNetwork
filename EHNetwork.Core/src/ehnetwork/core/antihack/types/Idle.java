package ehnetwork.core.antihack.types;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.antihack.Detector;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class Idle extends MiniPlugin implements Detector
{
	private AntiHack Host;
	
	private HashMap<Player, Long> _idleTime = new HashMap<Player, Long>();		
	
	public Idle (AntiHack host)
	{
		super("Idle Detector", host.getPlugin());
		Host = host;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateFlyhack(PlayerMoveEvent event)
	{
		if (!Host.isEnabled())
			return;
		
		Player player = event.getPlayer();

		_idleTime.put(player, System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateFreeCam(UpdateEvent event)
	{
		if (!Host.isEnabled())
			return;
		
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			//100% Valid
			if (Host.isValid(player, true))
				continue;

			if (!_idleTime.containsKey(player))
				continue;

			if (!UtilTime.elapsed(_idleTime.get(player), Host.IdleTime))
				continue;

			//Host.addSuspicion(player, "Lag / Fly (Idle)");
			//player.kickPlayer(C.cGold + "Mineplex " + C.cRed + "Anti-Cheat   " + C.cWhite + "Kicked for Lag / Fly (Idle)");
			
			UtilPlayer.message(player, C.cRed + C.Bold + "Mineplex Anti-Cheat detected Lagging / Fly (Idle)");
			UtilPlayer.message(player, C.cRed + C.Bold + "You have been returned to Lobby.");
			Host.Portal.sendPlayerToServer(player, "Lobby");	
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_idleTime.remove(player);
	}
}
