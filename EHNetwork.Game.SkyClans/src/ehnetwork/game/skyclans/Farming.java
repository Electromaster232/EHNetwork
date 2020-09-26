package ehnetwork.game.skyclans;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Farming extends MiniPlugin
{
	public Farming(JavaPlugin plugin) 
	{
		super("Farming", plugin);
	}
	
	@EventHandler
	public void BlockBreak(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getBlock().getType() != Material.LEAVES)
			return;
		
		if (event.getPlayer().getItemInHand() != null)
			if (event.getPlayer().getItemInHand().getType() == Material.SHEARS)
				return;
		
		if (Math.random() > 0.9)
			event.getBlock().getWorld().dropItemNaturally(
					event.getBlock().getLocation().add(0.5, 0.5, 0.5), 
					ItemStackFactory.Instance.CreateStack(Material.APPLE));
		
		if (Math.random() > 0.999)
			event.getBlock().getWorld().dropItemNaturally(
					event.getBlock().getLocation().add(0.5, 0.5, 0.5), 
					ItemStackFactory.Instance.CreateStack(Material.GOLDEN_APPLE));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void BlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;

		if (
				event.getBlock().getTypeId() != 59 &&
				event.getBlock().getTypeId() != 83 &&
				event.getBlock().getTypeId() != 104 &&
				event.getBlock().getTypeId() != 105 &&
				event.getBlock().getTypeId() != 127 &&
				event.getBlock().getTypeId() != 141 &&
				event.getBlock().getTypeId() != 142
				)
			return;

		if (event.getBlock().getLocation().getY() < event.getBlock().getWorld().getSeaLevel() - 12)
		{
			UtilPlayer.message(event.getPlayer(), F.main(getName(), "You cannot plant " + 
					F.item(ItemStackFactory.Instance.GetName(event.getPlayer().getItemInHand(), true)) + " this deep underground."));
			event.setCancelled(true);
		}
		
		else if (event.getBlock().getLocation().getY() > event.getBlock().getWorld().getSeaLevel() + 24)
		{
			UtilPlayer.message(event.getPlayer(), F.main(getName(), "You cannot plant " + 
					F.item(ItemStackFactory.Instance.GetName(event.getPlayer().getItemInHand(), true)) + " at this altitude."));
			event.setCancelled(true);
		}
	}
}
