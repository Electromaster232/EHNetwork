package mineplex.core.gadget.gadgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.event.GadgetBlockEvent;
import mineplex.core.gadget.types.ItemGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class ItemPaintballGun extends ItemGadget
{
	private HashSet<Projectile> _balls = new HashSet<Projectile>();
	
	public ItemPaintballGun(GadgetManager manager) 
	{
		super(manager, "Paintball Gun", new String[] 
				{
				C.cWhite + "PEW PEW PEW PEW!",
				},
				-1,
				Material.GOLD_BARDING, (byte)0,
				200, new Ammo("Paintball Gun", "100 Paintballs", Material.GOLD_BARDING, (byte)0, new String[] { C.cWhite + "100 Paintballs for you to shoot!" }, 500, 100));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		Projectile proj = player.launchProjectile(EnderPearl.class);
		proj.setVelocity(proj.getVelocity().multiply(2));
		_balls.add(proj);
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.5f, 1.2f);
	}
	
	@EventHandler
	public void Paint(ProjectileHitEvent event)
	{
		if (!_balls.remove(event.getEntity()))
			return;
		
		Location loc = event.getEntity().getLocation().add(event.getEntity().getVelocity());
		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 49);
		
		byte color = 2;
		double r = Math.random();
		if (r > 0.8) color = 4;
		else if (r > 0.6) color = 5;
		else if (r > 0.4) color = 9;
		else if (r > 0.2) color = 14;

		for (Block block : UtilBlock.getInRadius(loc, 3d).keySet())
		{
			if (block.getType() == Material.PORTAL)
				return;
			
			if (block.getType() == Material.CACTUS)
				return;
			
			if (block.getType() == Material.SUGAR_CANE_BLOCK)
				return;
		}
		
		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(UtilBlock.getInRadius(loc, 1.5d).keySet());
		
		GadgetBlockEvent gadgetEvent = new GadgetBlockEvent(this, blocks);
		Bukkit.getServer().getPluginManager().callEvent(gadgetEvent);
		
		if (gadgetEvent.isCancelled())
			return;
		
		for (Block block : gadgetEvent.getBlocks())
		{
			if (!UtilBlock.solid(block))
				continue;
			
			if (block.getType() == Material.CARPET)
				Manager.getBlockRestore().Add(block, 171, color, 4000);
			else
				Manager.getBlockRestore().Add(block, 35, color, 4000);
		}
	}
	
	@EventHandler
	public void Teleport(PlayerTeleportEvent event)
	{
		if (event.getCause() == TeleportCause.ENDER_PEARL)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void cleanupBalls(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		for (Iterator<Projectile> ballIterator = _balls.iterator(); ballIterator.hasNext();)
		{
			Projectile ball = ballIterator.next();
			
			if (ball.isDead() || !ball.isValid())
				ballIterator.remove();
		}
	}
}
