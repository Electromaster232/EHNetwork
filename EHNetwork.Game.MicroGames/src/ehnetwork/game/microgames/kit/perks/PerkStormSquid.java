package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkStormSquid extends SmashPerk
{
	public PerkStormSquid() 
	{
		super("Storm Squid", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		
	}

	@Override
	public void removeSuperCustom(Player player)
	{
		player.setFlying(false);
	}
	
	@EventHandler(priority = EventPriority.LOW)	//Happen before activation of Super
	public void lightningStrike(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();

		if (!isSuperActive(player))
			return;

		if (event.getAction() == Action.PHYSICAL)
			return;

		Block block = player.getTargetBlock(null, 100);
		if (block == null)
			return;
		
		final Location loc = block.getLocation().add(0.5, 0.5, 0.5);
		
		if (!Recharge.Instance.use(player, GetName() + " Strike", 1600, false, false))
			return;
		
		//Warning
		UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, loc.clone().add(0, 0.5, 0), 1f, 1f, 1f, 0.1f, 40,
				ViewDist.MAX, UtilServer.getPlayers());
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, loc.clone().add(0, 0.5, 0), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				//Warning
				player.getWorld().spigot().strikeLightningEffect(loc, false);
				
				HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(loc, 8);
				for (LivingEntity cur : targets.keySet())
				{
					if (cur.equals(player))
						continue;

					//Damage Event
					Manager.GetDamage().NewDamageEvent(cur, player, null, 
							DamageCause.CUSTOM, 20 * targets.get(cur), false, true, false,
							player.getName(), GetName());	
					
					//Velocity
					UtilAction.velocity(cur, UtilAlg.getTrajectory(loc, cur.getLocation()), 
							3 * targets.get(cur), false, 0, 1 * targets.get(cur), 2, true);
				}
			}
		}, 10);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void attackCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		if (!isSuperActive(player))
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		event.SetCancelled("Wither Form Melee Cancel");
	}

	@EventHandler
	public void flight(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : ((SmashKit)Kit).getSuperActive())
		{
			if (player.isFlying())
				continue;

			player.setAllowFlight(true);
			player.setFlying(true);
		}
	}
	
	@EventHandler
	public void flightBump(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : ((SmashKit)Kit).getSuperActive())
		{
			ArrayList<Location> collisions = new ArrayList<Location>();	

			//Bump
			for (Block block : UtilBlock.getInRadius(player.getLocation().add(0, 0.5, 0), 1.5d).keySet())
			{
				if (!UtilBlock.airFoliage(block))
				{
					collisions.add(block.getLocation().add(0.5, 0.5, 0.5));
				}
			}

			Vector vec = UtilAlg.getAverageBump(player.getLocation(), collisions);

			if (vec == null)
				continue;

			UtilAction.velocity(player, vec, 0.6, false, 0, 0.4, 10, true);
		}
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 3);
	}
}
