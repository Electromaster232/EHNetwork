package ehnetwork.game.arcade.game.games.milkcow;

import org.bukkit.entity.Player;

public class CowScore 
{
	public org.bukkit.entity.Player Player;
	public double Score;
	
	public CowScore(Player player, double i) 
	{
		Player = player;
		Score = i;
	}
}
