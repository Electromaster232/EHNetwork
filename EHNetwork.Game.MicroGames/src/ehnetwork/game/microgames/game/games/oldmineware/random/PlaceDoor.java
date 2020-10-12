package ehnetwork.game.microgames.game.games.oldmineware.random;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.OrderPlace;

public class PlaceDoor extends OrderPlace
{
	public PlaceDoor(OldMineWare host) 
	{
		super(host, "Place a wooden door", 64, -1, 1);
	}

	@Override
	public void Initialize()
	{
		
	}

	@Override
	public void Uninitialize()
	{
		
	}
}
