package ehnetwork.game.arcade.game.games.oldmineware.random;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.game.games.oldmineware.OldMineWare;
import ehnetwork.game.arcade.game.games.oldmineware.order.Order;

public class StandShelter extends Order 
{
	public StandShelter(OldMineWare host) 
	{
		super(host, "take shelter from rain");
	}

	@Override
	public void Initialize() 
	{
		
	}

	@Override
	public void Uninitialize()
	{
		Host.WorldData.World.setStorm(false);
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
		
		Host.WorldData.World.setStorm(true);
		
		for (Player player : Host.GetPlayers(true))
		{
			Block block = player.getLocation().add(0, 2, 0).getBlock();
			
			while (block.getTypeId() == 0 && block.getLocation().getY() < 255)
			{
				block = block.getRelative(BlockFace.UP);
				
				if (block.getTypeId() != 0)
				{
					SetCompleted(player);
					break;
				}
			}	
		}
			
	}
}
