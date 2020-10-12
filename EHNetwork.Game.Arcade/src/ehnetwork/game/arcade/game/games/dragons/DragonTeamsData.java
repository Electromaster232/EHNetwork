package ehnetwork.game.arcade.game.games.dragons;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilTime;

public class DragonTeamsData 
{
	public DragonsTeams Host;

	public EnderDragon Dragon;  
	
	public Entity TargetEntity = null;
  
	public Location Target = null;
	public Location Location = null;

	public float Pitch = 0;
	public Vector Velocity = new Vector(0,0,0);
	
	public double RangeBest = 1000;
	public long RangeTime = 0;
	
	public DragonTeamsData(DragonsTeams host, EnderDragon dragon) 
	{
		Host = host; 

		Dragon = dragon; 

		Velocity = dragon.getLocation().getDirection().setY(0).normalize();
		Pitch = UtilAlg.GetPitch(dragon.getLocation().getDirection());

		Location = dragon.getLocation();
	}
	
	public void Move()
	{
		Turn();

		Location.add(Velocity);
		Location.add(0, -Pitch, 0);

		Location.setPitch(-1 * Pitch);
		Location.setYaw(180 + UtilAlg.GetYaw(Velocity));

		Dragon.teleport(Location);
	}

	private void Turn() 
	{
		//Pitch
		float desiredPitch = UtilAlg.GetPitch(UtilAlg.getTrajectory(Location, Target));
		if (desiredPitch < Pitch)	Pitch = (float)(Pitch - 0.05);
		if (desiredPitch > Pitch)	Pitch = (float)(Pitch + 0.05);
		if (Pitch > 0.5)	Pitch = 0.5f;
		if (Pitch < -0.5)	Pitch = -0.5f;

		//Flat
		Vector desired = UtilAlg.getTrajectory2d(Location, Target);
		desired.subtract(UtilAlg.Normalize(new Vector(Velocity.getX(), 0, Velocity.getZ())));
		desired.multiply(0.075);

		Velocity.add(desired);

		//Speed
		UtilAlg.Normalize(Velocity);			
	}

	public void Target() 
	{		
		if (TargetEntity != null)
		{
			if (!TargetEntity.isValid())
			{
				TargetEntity = null;
			}
			else
			{
				Target = TargetEntity.getLocation().subtract(0, 8, 0);
			}
	
			return;
		}
		
		if (Target == null)
		{
			TargetSky();
		}
		
		if (UtilMath.offset(Location, Target) < 4)
		{
			//Target Player
			if (Target.getY() >= Host.GetSpectatorLocation().getY())
			{
				TargetPlayer();
			}
			//Target Sky
			else
			{
				TargetSky();
			}
		}
		
		TargetTimeout();
	}
	
	public void TargetTimeout()
	{
		if (UtilMath.offset(Location, Target)+1 < RangeBest)
		{
			RangeTime = System.currentTimeMillis();
			RangeBest = UtilMath.offset(Location, Target);
		}
		else
		{
			if (UtilTime.elapsed(RangeTime, 10000))
			{
				TargetSky();
			}
		}
	}
	
	public void TargetSky()
	{
		RangeBest = 9000;
		RangeTime = System.currentTimeMillis();
		
		Target = Host.GetSpectatorLocation().clone().add(50 - UtilMath.r(100), 20 + UtilMath.r(30), 50 - UtilMath.r(100));
	}
	
	public void TargetPlayer()
	{
		RangeBest = 9000;
		RangeTime = System.currentTimeMillis();
		
		Player player = Host.GetPlayers(true).get(UtilMath.r(Host.GetPlayers(true).size()));
		Target =  player.getLocation();

		Target.add(UtilAlg.getTrajectory(Location, Target).multiply(4));
	}
	
	public void HitByArrow()
	{
		TargetSky();
	}
}
