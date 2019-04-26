package mineplex.core.antihack.types;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import mineplex.core.MiniPlugin;
import mineplex.core.antihack.AntiHack;
import mineplex.core.antihack.Detector;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilTime;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed extends MiniPlugin implements Detector
{
	private AntiHack Host;
	
	private HashMap<Player, Entry<Integer, Long>> _speedTicks = new HashMap<Player, Entry<Integer, Long>>();				//Ticks, PrevY
	
	public Speed (AntiHack host)
	{
		super("Speed Detector", host.getPlugin());
		Host = host;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateSpeedhack(PlayerMoveEvent event)
	{
		if (!Host.isEnabled())
			return;
		
		Player player = event.getPlayer();

		//100% Valid
		if (Host.isValid(player, false))
			return; 

		UpdateSpeed(player, event);
	}
	
	private void UpdateSpeed(Player player, PlayerMoveEvent event) 
	{
		int count = 0;

		if (_speedTicks.containsKey(player))
		{	
			double offset;
			if (event.getFrom().getY() > event.getTo().getY())
			{
				offset = UtilMath.offset2d(event.getFrom(), event.getTo());
			}
			else
			{
				offset = UtilMath.offset(event.getFrom(), event.getTo());
			}

			//Limit
			double limit = 0.74;
			if (UtilEnt.isGrounded(player))
				limit = 0.32;

			for (PotionEffect effect : player.getActivePotionEffects())
			{
				if (effect.getType().equals(PotionEffectType.SPEED))
				{	
					if (UtilEnt.isGrounded(player))
						limit += 0.08 * (effect.getAmplifier() + 1);
					else
						limit += 0.04 * (effect.getAmplifier() + 1);
				}
			}

			//Check
			if (offset > limit && !UtilTime.elapsed(_speedTicks.get(player).getValue(), 100))//Counters Lag
			{
				count = _speedTicks.get(player).getKey() + 1;
			}
			else
			{
				count = 0;
			}
		}

		if (count > Host.SpeedHackTicks)
		{
			Host.addSuspicion(player, "Speed (Fly/Move)");
			count -= 2;
		}

		_speedTicks.put(player, new AbstractMap.SimpleEntry<Integer, Long>(count, System.currentTimeMillis()));
	}

	@Override
	public void Reset(Player player) 
	{
		_speedTicks.remove(player);
	}
}
