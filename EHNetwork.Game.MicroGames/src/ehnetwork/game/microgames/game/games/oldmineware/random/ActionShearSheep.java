package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;

public class ActionShearSheep extends Order 
{
	public ActionShearSheep(OldMineWare host) 
	{
		super(host, "shear a sheep");
	}

	@Override
	public void Initialize() 
	{
		for (Player player : Host.GetPlayers(true))
		{
			if (!player.getInventory().contains(Material.SHEARS))
				player.getInventory().addItem(new ItemStack(Material.SHEARS));	
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
	public void Update(PlayerShearEntityEvent event)
	{		
		SetCompleted(event.getPlayer());
	}
}
