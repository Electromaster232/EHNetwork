package ehnetwork.minecraft.game.classcombat.item.Throwable;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemUsable;

public class ProximityExplosive extends ItemUsable
{
	private HashMap<Entity, LivingEntity> _armed = new HashMap<Entity, LivingEntity>();
	
	public ProximityExplosive(ItemFactory factory, Material type,
							  int amount, boolean canDamage, int gemCost, int tokenCost,
							  ActionType useAction, boolean useStock, long useDelay,
							  int useEnergy, ActionType throwAction, boolean throwStock,
							  long throwDelay, int throwEnergy, float throwPower,
							  long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup)
	{
		super(factory, "Proximity Explosive", new String[] { 
				"Thrown Item:", 
				"Activates after 4 seconds.", 
				"Detonates on player proximity;",
				"* 8 Range" ,
				"* 1 Damage" ,
				"* Strong Knockback" ,
				"All effects scale down with range."
				}, type, amount, canDamage, gemCost, tokenCost,
				useAction, useStock, useDelay, useEnergy, throwAction, throwStock,
				throwDelay, throwEnergy, throwPower, 
				throwExpire, throwPlayer, throwBlock, throwIdle, throwPickup);
	}

	@Override
	public void UseAction(PlayerInteractEvent event) 
	{

	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{

	}

	@Override
	public void Idle(ProjectileUser data) 
	{

	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		//Arm
		_armed.put(data.GetThrown(), data.GetThrower());
		
		//Effect
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 7);
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.NOTE_PLING, 0.5f, 2f);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (UtilPlayer.isSpectator(event.getPlayer()))
			return;
		
		if (((CraftPlayer)event.getPlayer()).getHandle().spectating)
			return;
		
		if (_armed.containsKey(event.getItem()))
		{
			event.setCancelled(true);
			Detonate(event.getItem());
		}			
	}
	
	@EventHandler
	public void HopperPickup(InventoryPickupItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (_armed.containsValue(event.getItem()))
			event.setCancelled(true);
	}
	
	public void Detonate(Entity ent)
	{
		//Remove
		ent.remove();
		LivingEntity thrower = _armed.remove(ent);
	
		//Blast
		HashMap<Player, Double> hit = UtilPlayer.getInRadius(ent.getLocation(), 8);
		for (Player player : hit.keySet())
		{
			//Velocity
			UtilAction.velocity(player, UtilAlg.getTrajectory(ent.getLocation(), 
					player.getEyeLocation()), 2.4*hit.get(player), false, 0, 0.6*hit.get(player), 1.6, true);
			
			//Damage Event
			Factory.Damage().NewDamageEvent(player, thrower, null, 
					DamageCause.CUSTOM, 10*hit.get(player), false, true, false,
					UtilEnt.getName(thrower), GetName());
			
			//Inform
			UtilPlayer.message(player, F.main(GetName(), F.name(UtilEnt.getName(thrower)) +" hit you with " + F.item(GetName()) + "."));
		}
		
		//Effect
		ent.getWorld().playSound(ent.getLocation(), Sound.NOTE_PLING, 0.5f, 0.5f);
		ent.getWorld().playSound(ent.getLocation(), Sound.EXPLODE, 4f, 0.8f);
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, ent.getLocation(), 0, 0.5f, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
	}
	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		HashSet<Entity> expired = new HashSet<Entity>();
		
		for (Entity ent : _armed.keySet())
		{
			if (ent.isDead() || !ent.isValid() || ent.getTicksLived() >= 3600)
				expired.add(ent);
		}
		
		for (Entity ent : expired)
		{
			Detonate(ent);
		}		
	}
}

