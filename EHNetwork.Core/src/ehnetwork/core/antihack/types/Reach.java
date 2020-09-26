package ehnetwork.core.antihack.types;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.antihack.AntiHack;
import ehnetwork.core.antihack.Detector;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class Reach extends MiniPlugin implements Detector
{
	private AntiHack Host;

	private HashMap<Player, ArrayList<Location>> _history = new HashMap<Player, ArrayList<Location>>();			

	public Reach (AntiHack host)
	{
		super("Reach Detector", host.getPlugin());
		Host = host;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void recordMove(UpdateEvent event)
	{
		if (!Host.isEnabled())
			return;
		
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (player.getGameMode() != GameMode.SURVIVAL || UtilPlayer.isSpectator(player))
				continue;
			
			if (!_history.containsKey(player))
				_history.put(player, new ArrayList<Location>());

			_history.get(player).add(0, player.getLocation());

			while (_history.get(player).size() > 40)
			{
				_history.get(player).remove(_history.get(player).size()-1);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void reachDetect(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		if (!(event.getEntity() instanceof Player))
			return;
		
		LivingEntity damagerEntity = UtilEvent.GetDamagerEntity(event, false);
 
		if (!(damagerEntity instanceof Player))
			return;

		Player damager = (Player)damagerEntity;
		Player damagee = (Player)event.getEntity();
		
		if (!isInRange(damager.getLocation(), damagee.getLocation()))
		{
			ArrayList<Location> damageeHistory = _history.get(damagee);
			
			if (damageeHistory != null)
			{
				for (Location historyLoc : damageeHistory)
				{
					if (isInRange(damager.getLocation(), historyLoc))
					{
						return;
					}
				}
			}
			
			//Host.addSuspicion(damager, "Reach");
		}
	}

	private boolean isInRange(Location a, Location b)
	{
		//2d Range
		double distFlat = UtilMath.offset2d(a, b);		//Limit is 3.40
		if (distFlat > 3.4)
		{
			return true;
		}
		
		//3d Range
		double dist = UtilMath.offset(a, b);			//Limit is 6 (highest i saw was 5.67)
		if (dist > 6.0)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void Reset(Player player) 
	{
		_history.remove(player);
	}
}
