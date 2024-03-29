package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;

public class RidePig extends Order 
{
	public RidePig(OldMineWare host) 
	{
		super(host, "ride a pig");
	}

	@Override
	public void Initialize() 
	{
		for (Player player : Host.GetPlayers(true))
		{
			if (!player.getInventory().contains(Material.SADDLE))
				player.getInventory().addItem(new ItemStack(Material.SADDLE));	
		}
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
			if (player.isInsideVehicle())
				if (player.getVehicle() instanceof Pig)
					SetCompleted(player);
	}
}
