package ehnetwork.game.microgames.game.games.evolution;

import org.bukkit.entity.Player;

public class EvoScore 
{
	public org.bukkit.entity.Player Player;
	public int Kills;
	
	public EvoScore(Player player, int i) 
	{
		Player = player;
		Kills = i;
	}
}
