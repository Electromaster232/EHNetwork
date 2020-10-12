package ehnetwork.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.game.arcade.kit.perks.data.EarthquakeData;

public class PerkEarthquake extends SmashPerk
{
	private ArrayList<EarthquakeData> _night = new ArrayList<EarthquakeData>();
	
	public PerkEarthquake() 
	{
		super("Earthquake", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		_night.add(new EarthquakeData(player));
	}
		
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<EarthquakeData> quakeIter = _night.iterator();
		
		while (quakeIter.hasNext())
		{
			EarthquakeData data = quakeIter.next();
			
			//Expire
			if (UtilTime.elapsed(data.Time, 16000))
			{
				quakeIter.remove();
				continue;
			}
			
			for (Player player : Manager.GetGame().GetPlayers(true))
			{
				player.playSound(player.getLocation(), Sound.MINECART_BASE, 0.2f, 0.2f);
				
				if (player.equals(data.Player))
					continue;
				
				if (UtilEnt.isGrounded(player))
				{
					//Damage Event
					Manager.GetDamage().NewDamageEvent(player, data.Player, null, 
							DamageCause.CUSTOM, 1 + 2 * Math.random(), false, false, false,
							player.getName(), GetName());	
					
					//Velocity
					if (Recharge.Instance.use(player, GetName() + " Hit", 400, false, false))
						UtilAction.velocity(player, new Vector(Math.random() - 0.5, Math.random() * 0.2, Math.random() - 0.5), 
							Math.random() * 1 + 1, false, 0, 0.1 + Math.random() * 0.2, 2, true);
				}
				
				//Effect
				for (Block block : UtilBlock.getInRadius(player.getLocation(), 5).keySet())
				{
					if (Math.random() < 0.98)
						continue;
						
					if (!UtilBlock.solid(block))
						continue;
					
					if (!UtilBlock.airFoliage(block.getRelative(BlockFace.UP)))
						continue;
					
					player.playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
				}
			}
		}
	}
}
