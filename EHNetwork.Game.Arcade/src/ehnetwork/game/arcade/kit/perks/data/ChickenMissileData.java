package ehnetwork.game.arcade.kit.perks.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
public class ChickenMissileData 
{
	public Player Player;
	public Entity Chicken;
	public Vector Direction;
	public long Time;
	
	public double LastX;
	public double LastY;
	public double LastZ;
	
	public ChickenMissileData(Player player, Entity chicken)
	{
		Player = player;
		Chicken = chicken;
		Direction = player.getLocation().getDirection().multiply(0.6);
		Time = System.currentTimeMillis();
	}
	
	public boolean HasHitBlock()
	{
		//Not First Run
		if (LastX != 0 && LastY != 0 && LastZ != 0)
		{
			if (Math.abs(Chicken.getLocation().getX() - LastX) < Math.abs(Direction.getX()/10d))
			{
				return true;
			}
			if (Math.abs(Chicken.getLocation().getY() - LastY) < Math.abs(Direction.getY()/10d))
			{
				if (Direction.getY() > 0 || -0.02 > Direction.getY())
				{
					return true;
				}
			}
			if (Math.abs(Chicken.getLocation().getZ() - LastZ) < Math.abs(Direction.getZ()/10d))
			{	
				return true;
			}
		}
		
		LastX = Chicken.getLocation().getX();
		LastY = Chicken.getLocation().getY();
		LastZ = Chicken.getLocation().getZ();
		
		return false;
	}
}
