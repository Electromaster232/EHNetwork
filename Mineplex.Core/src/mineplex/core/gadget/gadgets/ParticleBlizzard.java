package mineplex.core.gadget.gadgets;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import mineplex.core.blood.BloodEvent;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.gadget.types.ParticleGadget;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;

public class ParticleBlizzard extends ParticleGadget
{
	private HashSet<Arrow> _arrows = new HashSet<Arrow>();

	public ParticleBlizzard(GadgetManager manager)
	{
		super(manager, "Frost Lord", new String[] 
				{
				C.cWhite + "You are a mighty frost lord.",
				C.cWhite + "Your double jumps and arrows",
				C.cWhite + "are enchanted with snow powers.",
				" ",
				C.cPurple + "No longer available",
				},
				-1,
				Material.SNOW_BALL, (byte)0);
	}

	@EventHandler
	public void playParticle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (!shouldDisplay(player))
				continue;
			
			if (Manager.isMoving(player))
			{
				UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, player.getLocation().add(0, 1, 0), 0.2f, 0.2f, 0.2f, 0, 4,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				player.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_RAIN, 0.015f, 0.2f);
				
				double scale = (double)(player.getTicksLived() % 50) / 50d;
				
				for (int i = 0 ; i < 8 ; i++)
				{
					double r = (1d-scale) * Math.PI * 2;
					
					double x = Math.sin(r + (i * (Math.PI/4))) * (r%(Math.PI * 4)) * 0.4;
					double z = Math.cos(r + (i * (Math.PI/4))) * (r%(Math.PI * 4)) * 0.4;
					
					UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, player.getLocation().add(x, scale * 3, z), 0f, 0f, 0f, 0, 1,
							ViewDist.NORMAL, UtilServer.getPlayers());
					
					if (scale > 0.95 && Recharge.Instance.use(player, GetName(), 1000, false, false))
					{
						UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, player.getLocation().add(0, scale * 3.5, 0), 0f, 0f, 0f, 0.2f, 60,
								ViewDist.NORMAL, UtilServer.getPlayers());
						player.getWorld().playSound(player.getLocation(), Sound.STEP_SNOW, 1f, 1.5f);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void jump(PlayerToggleFlightEvent event)
	{
		if (!shouldDisplay(event.getPlayer()))
			return;
		
		if (!event.getPlayer().isFlying())
			if (IsActive(event.getPlayer()))
				UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, event.getPlayer().getLocation(), 0f, 0f, 0f, 0.6f, 100,
						ViewDist.NORMAL, UtilServer.getPlayers());
	} 
	
	@EventHandler
	public void arrow(ProjectileLaunchEvent event)
	{
		if (Manager.hideParticles())
			return;
		
		if (event.getEntity() instanceof Arrow)
		{
			if (event.getEntity().getShooter() != null)
			{
				if (GetActive().contains(event.getEntity().getShooter()))
				{
					_arrows.add((Arrow)event.getEntity());
				}
			}
		}
	} 
	
	@EventHandler
	public void arrow(ProjectileHitEvent event)
	{
		if (!_arrows.remove(event.getEntity()))
			return;
		
		UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, event.getEntity().getLocation(), 0f, 0f, 0f, 0.4f, 12,
				ViewDist.NORMAL, UtilServer.getPlayers());
	}
	
	@EventHandler
	public void arrowClean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Iterator<Arrow> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Arrow arrow = arrowIterator.next();
			
			if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround())
			{
				arrowIterator.remove();
			}
			else
			{
				UtilParticle.PlayParticle(ParticleType.SNOW_SHOVEL, arrow.getLocation(), 0f, 0f, 0f, 0f, 1,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
	}

	@EventHandler
	public void death(BloodEvent event)
	{
		if (event.getPlayer() == null)
			return;
		
		if (!IsActive(event.getPlayer()))
			return;
		
		if (!shouldDisplay(event.getPlayer()))
			return;
			
		event.setItem(Material.SNOW_BALL, (byte)0);
	}
}
