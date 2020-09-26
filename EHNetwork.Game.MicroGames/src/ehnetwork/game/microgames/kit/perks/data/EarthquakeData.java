package ehnetwork.game.microgames.kit.perks.data;

import org.bukkit.entity.Player;

public class EarthquakeData
{
	public Player Player;
	public long Time;

	public EarthquakeData(Player player)
	{
		Player = player;
		Time = System.currentTimeMillis();
	}
}
