package ehnetwork.minecraft.game.classcombat.item.Throwable;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemUsable;

public class PoisonBall extends ItemUsable
{
	public PoisonBall(ItemFactory factory, Material type,
					  int amount, boolean canDamage, int gemCost, int tokenCost,
					  ActionType useAction, boolean useStock, long useDelay,
					  int useEnergy, ActionType throwAction, boolean throwStock,
					  long throwDelay, int throwEnergy, float throwPower,
					  long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup)
	{
		super(factory, "Poison Ball", new String[] { "Thrown:", "Poisons for 6 seconds on hit.", "Bounces back to you.", "Can be intercepted by enemy on return." }, type, amount, canDamage, gemCost, tokenCost,
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
		Rebound(data.GetThrower(), data.GetThrown());
		
		if (target == null)
			return;

		Factory.Condition().Factory().Poison(GetName(), target, data.GetThrower(), 6, 0, false, true, true);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Rebound(data.GetThrower(), data.GetThrown());
	}
	
	public void Rebound(LivingEntity player, Entity ent)
	{
		ent.getWorld().playSound(ent.getLocation(), Sound.ITEM_PICKUP, 1f, 0.5f);
		
		double mult = 0.5 + (0.6 * (UtilMath.offset(player.getLocation(), ent.getLocation())/16d));
		
		//Velocity
		ent.setVelocity(player.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().add(new Vector(0, 0.4, 0)).multiply(mult));
		
		//Ticks
		if (ent instanceof Item)
			((Item)ent).setPickupDelay(5);
	}
}
