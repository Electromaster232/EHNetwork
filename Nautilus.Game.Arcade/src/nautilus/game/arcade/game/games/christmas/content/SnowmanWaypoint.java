package nautilus.game.arcade.game.games.christmas.content;

import org.bukkit.Location;

public class SnowmanWaypoint
{
	public Location Last;
	public Location Target;
	public long Time;
	
	public SnowmanWaypoint(Location last)
	{
		Last = last;
		Target = null;
		Time = System.currentTimeMillis();
	}
}
