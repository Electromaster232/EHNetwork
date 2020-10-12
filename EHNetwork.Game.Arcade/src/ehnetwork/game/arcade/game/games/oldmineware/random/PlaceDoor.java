package ehnetwork.game.arcade.game.games.oldmineware.random;

import ehnetwork.game.arcade.game.games.oldmineware.OldMineWare;
import ehnetwork.game.arcade.game.games.oldmineware.order.OrderPlace;

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
