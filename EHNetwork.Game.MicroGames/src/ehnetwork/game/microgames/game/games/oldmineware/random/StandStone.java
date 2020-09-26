package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;

public class StandStone extends Order 
{
	public StandStone(OldMineWare host) 
	{
		super(host, "Stand on stone");
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
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STONE)
				SetCompleted(player);
	}
}
