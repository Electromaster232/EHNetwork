package ehnetwork.minecraft.game.classcombat.item.Throwable;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.minecraft.game.classcombat.item.ItemFactory;
import ehnetwork.minecraft.game.classcombat.item.ItemUsable;

public class WaterBottle extends ItemUsable
{
	public WaterBottle(ItemFactory factory, Material type,
					   int amount, boolean canDamage, int gemCost, int tokenCost,
					   ActionType useAction, boolean useStock, long useDelay,
					   int useEnergy, ActionType throwAction, boolean throwStock,
					   long throwDelay, int throwEnergy, float throwPower,
					   long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup)
	{
		super(factory, "Water Bottle", new String[] { 
				"Thrown, giving AoE effect;", 
				"* 3 Range",
				"* Douses Players",
				"* Extinguishes Fires",
				"Used, giving personal effect;", 
				"* Douses Player",
				"* Fire Resistance for 4 Seconds"
				}, type, amount, canDamage, gemCost, tokenCost,
				useAction, useStock, useDelay, useEnergy, throwAction, throwStock,
				throwDelay, throwEnergy, throwPower, 
				throwExpire, throwPlayer, throwBlock, throwIdle, throwPickup);
		
		setFree(true);
	}

	@Override
	public void UseAction(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		
		if (((CraftPlayer)player).getHandle().spectating)
			return;

		//Extinguish
		player.setFireTicks(-20);
		
		//Resist
		Factory.Condition().Factory().FireResist(GetName(), player, player, 4, 0, false, true, true);

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.SPLASH, 1f, 1.4f);
		player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, 8);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Break(data);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Break(data);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Break(data);
	}

	public void Break(ProjectileUser data)
	{
		//Splash
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 20);
		data.GetThrown().getWorld().playEffect(data.GetThrown().getLocation(), Effect.STEP_SOUND, 8);
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.SPLASH, 1f, 1.4f);

		//Extinguish
		Factory.Fire().RemoveNear(data.GetThrown().getLocation(), 3);
		
		//Remove
		data.GetThrown().remove();

		for (Player player : UtilPlayer.getNearby(data.GetThrown().getLocation(), 3))
		{
			//Extinguish
			player.setFireTicks(-20);
		}
	}
}
