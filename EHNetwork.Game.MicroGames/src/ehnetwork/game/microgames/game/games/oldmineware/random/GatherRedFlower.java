package ehnetwork.game.microgames.game.games.oldmineware.random;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.OrderGather;

public class GatherRedFlower extends OrderGather
{
	public GatherRedFlower(OldMineWare host) 
	{
		super(host, "Pick 3 Red Roses", 38, -1, 3);
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
