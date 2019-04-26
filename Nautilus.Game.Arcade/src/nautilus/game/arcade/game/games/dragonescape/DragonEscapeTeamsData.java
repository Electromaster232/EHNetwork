package nautilus.game.arcade.game.games.dragonescape;

import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.util.Vector;

public class DragonEscapeTeamsData 
{
	public DragonEscapeTeams Host;

	public EnderDragon Dragon;  

	public Location Target = null;
	public Location Location = null;

	public float Pitch = 0;
	public Vector Velocity = new Vector(0,0,0);

	public DragonEscapeTeamsData(DragonEscapeTeams host, EnderDragon dragon, Location target) 
	{
		Host = host; 

		Dragon = dragon; 
		UtilEnt.ghost(Dragon, true, false);

		Location temp = dragon.getLocation();
		temp.setPitch(UtilAlg.GetPitch(UtilAlg.getTrajectory(dragon.getLocation(), target)));
		dragon.teleport(temp);

		Velocity = dragon.getLocation().getDirection().setY(0).normalize();
		Pitch = UtilAlg.GetPitch(dragon.getLocation().getDirection());

		Location = dragon.getLocation(); 
	}

	public void Move()
	{
		Turn();

		//Speed
		double speed = 0.20;

		//speed += (System.currentTimeMillis() - Host.GetStateTime())/1000d * 0.001;

		//Speed Distance Boost
		/*
		if (!Host.GetScores().isEmpty())
		{
			double score = Host.GetScore(Dragon);
			double best = Host.GetScores().get(0).Score;
			
			double lead = (best-score)/10000d;
			
			speed += lead * 0.0025;
		}
		*/
		
		speed = speed * Host.GetSpeedMult();
		
		Location.add(Velocity.clone().multiply(speed));
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
		desired.multiply(0.2);

		Velocity.add(desired);

		//Speed
		UtilAlg.Normalize(Velocity);			
	}
}
