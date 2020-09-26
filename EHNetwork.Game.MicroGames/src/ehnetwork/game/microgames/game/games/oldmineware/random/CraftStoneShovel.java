package ehnetwork.game.microgames.game.games.oldmineware.random;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.OrderCraft;

public class CraftStoneShovel extends OrderCraft
{
	public CraftStoneShovel(OldMineWare host) 
	{
		super(host, "Craft a stone shovel", 273, -1, 1);
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
