package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilGear;
import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;

public class ActionMilkCow extends Order 
{
	public ActionMilkCow(OldMineWare host) 
	{
		super(host, "milk a cow");
	}

	@Override
	public void Initialize() 
	{
		for (Player player : Host.GetPlayers(true))
		{
			if (!player.getInventory().contains(Material.BUCKET))
				player.getInventory().addItem(new ItemStack(Material.BUCKET));	
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
	public void Update(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Cow))
			return;
		
		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), Material.BUCKET))
			return;
		
		SetCompleted(event.getPlayer());
	}
}
