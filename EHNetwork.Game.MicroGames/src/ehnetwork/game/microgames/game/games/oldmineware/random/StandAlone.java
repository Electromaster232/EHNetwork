package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;

public class StandAlone extends Order 
{
	public StandAlone(OldMineWare host) 
	{
		super(host, "Run away from everyone");
	}

	@Override
	public void Initialize() 
	{
		
	}

	@Override
	public void Uninitialize()
	{
		
	}

	@Override
	public void FailItems(Player player) 
	{
		
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		for (Player player : Host.GetPlayers(true))
		{
			boolean alone = true;
			
			for (Player other : Host.GetPlayers(true))
			{
				if (other.equals(player))
					continue;
				
				if (UtilMath.offset(player, other) < 16)
				{
					alone = false;
					break;
				}	
			}
			
			if (alone)
				SetCompleted(player);
		}		
	}
}
