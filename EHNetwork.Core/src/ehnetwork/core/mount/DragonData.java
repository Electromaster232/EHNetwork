package ehnetwork.core.mount;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEnderDragon;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;

public class DragonData 
{
	DragonMount Host;
	
	public EnderDragon Dragon; 
	public Player Rider;
	
	public Entity TargetEntity = null;
  
	public Location Location = null;

	public float Pitch = 0;
	public Vector Velocity = new Vector(0,0,0);

	public Entity Chicken;
	
	public DragonData(DragonMount dragonMount, Player rider) 
	{
		Host = dragonMount;
		
		Rider = rider; 

		Velocity = rider.getLocation().getDirection().setY(0).normalize();
		Pitch = UtilAlg.GetPitch(rider.getLocation().getDirection());

		Location = rider.getLocation();
	
		
		//Spawn Dragon
		Dragon = rider.getWorld().spawn(rider.getLocation(), EnderDragon.class);
		UtilEnt.Vegetate(Dragon);
		UtilEnt.ghost(Dragon, true, false);
		
		rider.getWorld().playSound(rider.getLocation(), Sound.ENDERDRAGON_GROWL, 20f, 1f);		
		
		Chicken = rider.getWorld().spawn(rider.getLocation(), Chicken.class);
		Dragon.setPassenger(Chicken);

		Chicken.setPassenger(Rider);
		
		Bukkit.getServer().getScheduler().runTaskLater(Host.Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				Chicken.setPassenger(Rider);
			}
		}, 10L);
	}
	
	public void Move()
	{
		Rider.eject();
		((CraftEnderDragon)Dragon).getHandle().setTargetBlock(GetTarget().getBlockX(), GetTarget().getBlockY(), GetTarget().getBlockZ());
	}
	
	public Location GetTarget()
	{
		return Rider.getLocation().add(Rider.getLocation().getDirection().multiply(40));
	}
}
