package nautilus.game.arcade.game.games.dragonriders;

import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import nautilus.game.arcade.ArcadeManager;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DragonData 
{
	ArcadeManager Manager;
	
	public EnderDragon Dragon; 
	public Player Rider;
	
	public Entity TargetEntity = null;
  
	public Location Location = null;

	public float Pitch = 0;
	public Vector Velocity = new Vector(0,0,0);
	
	public DragonData(ArcadeManager manager, Player rider) 
	{
		Manager = manager;
		
		Rider = rider; 

		Velocity = rider.getLocation().getDirection().setY(0).normalize();
		Pitch = UtilAlg.GetPitch(rider.getLocation().getDirection());

		Location = rider.getLocation();
		
		//Spawn Dragon
		manager.GetGame().CreatureAllowOverride = true;
		Dragon = rider.getWorld().spawn(rider.getLocation(), EnderDragon.class);
		UtilEnt.Vegetate(Dragon);
		manager.GetGame().CreatureAllowOverride = false;
		
		rider.getWorld().playSound(rider.getLocation(), Sound.ENDERDRAGON_GROWL, 20f, 1f);
		
		Dragon.setPassenger(Rider);
	}
	
	public void Move()
	{
		((CraftEnderDragon)Dragon).getHandle().setTargetBlock(GetTarget().getBlockX(), GetTarget().getBlockY(), GetTarget().getBlockZ());
		
		Manager.GetExplosion().BlockExplosion(UtilBlock.getInRadius(Dragon.getLocation(), 10d).keySet(), Dragon.getLocation(), false);
	}
	
	public Location GetTarget()
	{
		return Rider.getLocation().add(Rider.getLocation().getDirection().multiply(40));
	}
}
