package mineplex.minecraft.game.classcombat.item.Throwable;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import mineplex.core.projectile.ProjectileUser;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.minecraft.game.classcombat.item.ItemFactory;
import mineplex.minecraft.game.classcombat.item.ItemUsable;

public class ProximityZapper extends ItemUsable
{
	private HashMap<Entity, LivingEntity> _armed = new HashMap<Entity, LivingEntity>();

	public ProximityZapper(ItemFactory factory, Material type,
			int amount, boolean canDamage, int gemCost, int tokenCost,
			ActionType useAction, boolean useStock, long useDelay,
			int useEnergy, ActionType throwAction, boolean throwStock,
			long throwDelay, int throwEnergy, float throwPower, 
			long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup) 
	{
		super(factory, "Proximity Zapper", new String[] { 
				"Thrown Item:", 
				"Activates after 4 seconds.", 
				"Detonates on player proximity;",
				"* Lightning strikes the Zapper" ,
				"* Silence for 6 seconds" ,
				"* Shock for 6 seconds" ,
				"* Slow IV for 6 seconds"
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
			Detonate(event.getItem(), event.getPlayer());
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

	public void Detonate(Entity ent, Player player)
	{
		//Remove
		ent.remove();
		LivingEntity thrower = _armed.remove(ent);

		//Stun
		if (player != null)
		{
			//Inform
			UtilPlayer.message(player, F.main(GetName(), F.name(UtilEnt.getName(thrower)) +" hit you with " + F.item(GetName()) + "."));
		
			Factory.Condition().Factory().Silence(GetName(), player, thrower, 6, false, true);	
			Factory.Condition().Factory().Shock(GetName(), player, thrower, 6, false, false);
			Factory.Condition().Factory().Slow(GetName(), player, thrower, 6, 3, false, true, true, true);
		}

		//Blast
		ent.getWorld().strikeLightning(ent.getLocation());

		//Effect
		ent.getWorld().playSound(ent.getLocation(), Sound.NOTE_PLING, 0.5f, 0.5f);
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, ent.getLocation(), 0, 0.5f, 0, 0, 1,
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
			Detonate(ent, null);
		}		
	}
}

