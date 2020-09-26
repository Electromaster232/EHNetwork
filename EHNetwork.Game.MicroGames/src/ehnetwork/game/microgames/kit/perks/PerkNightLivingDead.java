package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.game.microgames.kit.perks.data.NightLivingDeadData;

public class PerkNightLivingDead extends SmashPerk
{
	private ArrayList<NightLivingDeadData> _night = new ArrayList<NightLivingDeadData>();
	
	private HashSet<Material> _ignoreList = new HashSet<Material>();;
	
	public PerkNightLivingDead() 
	{
		super("Night of the Living Dead", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		_night.add(new NightLivingDeadData(player));
	}
	
	@EventHandler
	public void timeUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (_night.isEmpty() && Manager.GetGame().WorldTimeSet != 12000)
		{
			Manager.GetGame().WorldTimeSet = (Manager.GetGame().WorldTimeSet + 50)%24000;
		}
		else if (!_night.isEmpty() && Manager.GetGame().WorldTimeSet != 18000)
		{
			Manager.GetGame().WorldTimeSet = (Manager.GetGame().WorldTimeSet + 50)%24000;
		}
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<NightLivingDeadData> nightIter = _night.iterator();
		
		while (nightIter.hasNext())
		{
			NightLivingDeadData data = nightIter.next();
			
			//Expire
			if (UtilTime.elapsed(data.Time, 30000))
			{
				nightIter.remove();
				
				for (Zombie zombie : data.Zombies)
					zombie.damage(1000);
				continue;
			}
			
			//Spawn
			if (UtilTime.elapsed(data.LastSpawn, 1000))
			{
				Location origin = UtilAlg.Random(Manager.GetGame().GetPlayers(true)).getLocation();
				Location loc = findSpawn(origin);
				
				if (Math.abs(loc.getY() - origin.getY()) > 6)
					continue;
				
				if (!UtilBlock.airFoliage(loc.getBlock()) || !UtilBlock.airFoliage(loc.getBlock().getRelative(BlockFace.UP)))
					continue;
				
				//Set Spawned
				data.LastSpawn = System.currentTimeMillis();
				
				//Move Down
				loc.subtract(0, 1, 0);
				
				//Spawn
				Manager.GetGame().CreatureAllowOverride = true;
				Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);
				Manager.GetGame().CreatureAllowOverride = false;
				
				data.Zombies.add(zombie);
				
				//Pop up
				zombie.setVelocity(new Vector(0,0.4,0));
				//zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 1, true));
			
				//Effect
				zombie.getWorld().playSound(zombie.getLocation(), Sound.ZOMBIE_IDLE, 1f, 0.75f);
				
				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
			}
		}
	}
	
	@EventHandler
	public void update(EntityTargetEvent event)
	{
		for (NightLivingDeadData data : _night)
		{
			if (data.Zombies.contains(event.getEntity()))
			{
				if (data.Player.equals(event.getTarget()))
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
	public Location findSpawn(Location area)
	{
		return UtilBlock.getHighest(area.getWorld(), (int)(area.getX() + Math.random() * 24 - 12), (int)(area.getZ() + Math.random() * 24 - 12), _ignoreList).getLocation().add(0.5, 0.5, 0.5);
	}
}
