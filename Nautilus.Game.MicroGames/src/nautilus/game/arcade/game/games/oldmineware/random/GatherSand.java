package nautilus.game.arcade.game.games.oldmineware.random;

import nautilus.game.arcade.game.games.oldmineware.OldMineWare;
import nautilus.game.arcade.game.games.oldmineware.order.OrderGather;

public class GatherSand extends OrderGather
{
	public GatherSand(OldMineWare host) 
	{
		super(host, "Pick up 16 Sand", 12, -1, 16);
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
