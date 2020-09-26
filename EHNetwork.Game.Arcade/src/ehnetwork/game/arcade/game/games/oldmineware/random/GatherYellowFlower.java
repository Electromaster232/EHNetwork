package ehnetwork.game.arcade.game.games.oldmineware.random;

import ehnetwork.game.arcade.game.games.oldmineware.OldMineWare;
import ehnetwork.game.arcade.game.games.oldmineware.order.OrderGather;

public class GatherYellowFlower extends OrderGather
{
	public GatherYellowFlower(OldMineWare host) 
	{
		super(host, "Pick 4 Yellow Flowers", 37, -1, 4);
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
