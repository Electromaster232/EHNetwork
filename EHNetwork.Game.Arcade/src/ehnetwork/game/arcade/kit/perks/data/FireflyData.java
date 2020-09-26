package ehnetwork.game.arcade.kit.perks.data;

import java.util.HashSet;

import org.bukkit.entity.Entity;

public class FireflyData 
{
	public org.bukkit.entity.Player Player;
	public org.bukkit.Location Location;
	public long Time;
	public HashSet<Entity> Targets = new HashSet<Entity>();
	
	public FireflyData(org.bukkit.entity.Player player)
	{
		Player = player;
		Location = player.getLocation();
		Time = System.currentTimeMillis();
	}
}
