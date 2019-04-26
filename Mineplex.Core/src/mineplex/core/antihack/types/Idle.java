package mineplex.core.antihack.types;

import java.util.HashMap;

import mineplex.core.MiniPlugin;
import mineplex.core.antihack.AntiHack;
import mineplex.core.antihack.Detector;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

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
