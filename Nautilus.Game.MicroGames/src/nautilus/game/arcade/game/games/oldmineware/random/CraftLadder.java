package nautilus.game.arcade.game.games.oldmineware.random;

import nautilus.game.arcade.game.games.oldmineware.OldMineWare;
import nautilus.game.arcade.game.games.oldmineware.order.OrderCraft;

public class CraftLadder extends OrderCraft
{
	public CraftLadder(OldMineWare host) 
	{
		super(host, "Craft some ladders", 65, -1, 1);
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
