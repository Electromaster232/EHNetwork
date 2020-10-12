package ehnetwork.minecraft.game.classcombat.item.Throwable;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemUsable;

public class Web extends ItemUsable
{
	public Web(ItemFactory factory, Material type,
			   int amount, boolean canDamage, int gemCost, int tokenCost,
			   ActionType useAction, boolean useStock, long useDelay,
			   int useEnergy, ActionType throwAction, boolean throwStock,
			   long throwDelay, int throwEnergy, float throwPower,
			   long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup)
	{
		super(factory, "Web", new String[] { "Thrown:", "Used to trap enemies." }, type, amount, canDamage, gemCost, tokenCost,
				useAction, useStock, useDelay, useEnergy, throwAction, throwStock,
				throwDelay, throwEnergy, throwPower, 
				throwExpire, throwPlayer, throwBlock, throwIdle, throwPickup);
		
		setFree(true);
	}

	@Override
	public void UseAction(PlayerInteractEvent event) 
	{

	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target != null)
		{
			double distance = UtilMath.offset(target.getLocation(), data.GetThrown().getLocation());
			
			if (distance > .75)
			{
				data.GetThrown().teleport(data.GetThrown().getLocation().add(new Vector(0, -distance / 2, 0)));
			}
		}
		
		CreateWeb(data.GetThrown());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		CreateWeb(data.GetThrown());
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		CreateWeb(data.GetThrown());
	}
	
	public void CreateWeb(Entity ent)
	{
		//Effect
		ent.getWorld().playEffect(ent.getLocation(), Effect.STEP_SOUND, 30);
		
		if (!UtilBlock.airFoliage(ent.getLocation().getBlock()))
			return;
		
		Factory.BlockRestore().Add(ent.getLocation().getBlock(), 30, (byte)0, 6000);
		
		ent.remove();
	}
}
