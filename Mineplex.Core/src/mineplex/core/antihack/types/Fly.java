package mineplex.core.antihack.types;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import mineplex.core.MiniPlugin;
import mineplex.core.antihack.AntiHack;
import mineplex.core.antihack.Detector;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilMath;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class Fly extends MiniPlugin implements Detector
{
	private AntiHack Host;
	
	private HashMap<Player, Entry<Integer, Double>> _floatTicks = new HashMap<Player, Entry<Integer, Double>>();			//Ticks, PrevY
	private HashMap<Player, Entry<Integer, Double>> _hoverTicks = new HashMap<Player, Entry<Integer, Double>>();			//Ticks, PrevY
	private HashMap<Player, Entry<Integer, Double>> _riseTicks = new HashMap<Player, Entry<Integer, Double>>();				//Ticks, PrevY	
	
	public Fly (AntiHack host)
	{
		super("Fly Detector", host.getPlugin());
		Host = host;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateFlyhack(PlayerMoveEvent event)
	{
		if (!Host.isEnabled())
			return;
		
		Player player = event.getPlayer();

		//100% Valid
		if (Host.isValid(player, true)) 
			Reset(player);

		//Hasn't moved, just looking around
		if (UtilMath.offset(event.getFrom(), event.getTo()) <= 0)
		{
			updateFloat(player);
			return;
		}
		else
		{
			_floatTicks.remove(player);
		}

		updateHover(player);
		updateRise(player);
	}
	
	private void updateFloat(Player player) 
	{
		int count = 0;

		if (_floatTicks.containsKey(player))
		{	
			if (player.getLocation().getY() == _floatTicks.get(player).getValue())
			{
				count = _floatTicks.get(player).getKey() + 1;
			}
			else
			{
				count = 0;
			}
		}

		if (count > Host.FloatHackTicks)
		{
			Host.addSuspicion(player, "Fly (Float)");
			count -= 2;
		}

		_floatTicks.put(player, new AbstractMap.SimpleEntry<Integer, Double>(count, player.getLocation().getY()));
	}

	private void updateHover(Player player) 
	{
		int count = 0;

		if (_hoverTicks.containsKey(player))
		{	
			if (player.getLocation().getY() == _hoverTicks.get(player).getValue())
			{
				count = _hoverTicks.get(player).getKey() + 1;
			}
			else
			{
				count = 0;
			}
			
			//player.sendMessage(count + " - " + player.getLocation().getY() + " vs " + _hoverTicks.get(player).getValue());
		}

		if (count > Host.HoverHackTicks)
		{
			Host.addSuspicion(player, "Fly (Hover)");
			count -= 2;
		}
	
		_hoverTicks.put(player, new AbstractMap.SimpleEntry<Integer, Double>(count, player.getLocation().getY()));
	}

	private void updateRise(Player player) 
	{
		int count = 0;

		if (_riseTicks.containsKey(player))
		{	
			if (player.getLocation().getY() >= _riseTicks.get(player).getValue())
			{
				boolean nearBlocks = false;
				for (Block block : UtilBlock.getSurrounding(player.getLocation().getBlock(), true))
				{
					if (block.getType() != Material.AIR)
					{
						nearBlocks = true;
						break;
					}
				}
				
				if (nearBlocks)
				{
					count = 0;
				}
				else
				{
					count = _riseTicks.get(player).getKey() + 1;
				}
					
			}
			else
			{
				count = 0;
			}
		}

		if (count > Host.RiseHackTicks)
		{
			//Only give Offense if actually rising - initial ticks can be trigged via Hover.
			if (player.getLocation().getY() > _riseTicks.get(player).getValue())
				Host.addSuspicion(player, "Fly (Rise)");
			
			count -= 2;
		}

		_riseTicks.put(player, new AbstractMap.SimpleEntry<Integer, Double>(count, player.getLocation().getY()));
	}
	
	@Override
	public void Reset(Player player) 
	{
		_floatTicks.remove(player);
		_hoverTicks.remove(player);
		_riseTicks.remove(player);
	}
}
