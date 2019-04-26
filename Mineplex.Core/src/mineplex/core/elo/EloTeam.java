package mineplex.core.elo;

import java.util.ArrayList;
import java.util.List;

public class EloTeam
{
	private List<EloPlayer> _players = new ArrayList<EloPlayer>();
	
	public int TotalElo = 0;
	
	public void addPlayer(EloPlayer player)
	{
		TotalElo += player.Rating;
		
		_players.add(player);
	}
	
	public List<EloPlayer> getPlayers()
	{
		return _players;
	}
}
