package ehnetwork.game.microgames.game.games.christmas.content;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;

import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilTime;

public class SnowmanMinion 
{
	public Snowman Ent;
	public Player Target;
	
	public Location OrbitLocation;
	
	public long StackDelay;
	public long AttackDelay;
	
	public SnowmanMinion(Snowman ent)
	{
		Ent = ent;
		UtilEnt.Vegetate(Ent);
		Ent.setMaxHealth(100);
		Ent.setHealth(Ent.getMaxHealth());
		
		StackDelay = 0;
		AttackDelay = System.currentTimeMillis();
	}

	public boolean CanStack() 
	{
		return UtilTime.elapsed(StackDelay, 8000);
	}
}
