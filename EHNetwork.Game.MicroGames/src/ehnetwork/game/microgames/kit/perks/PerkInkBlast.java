package ehnetwork.game.microgames.kit.perks;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkInkBlast extends SmashPerk implements IThrown
{
	public PerkInkBlast() 
	{
		super("Ink Shotgun", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Ink Shotgun"
				});
	}


	@EventHandler
	public void shoot(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;

		Player player = event.getPlayer();
		
		if (isSuperActive(player))
			return;

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 5000, true, true))
			return;

		event.setCancelled(true);

		UtilInv.Update(player);

		for (int i=0 ; i<7 ; i++)
		{
			org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(Material.INK_SACK));
			
			Vector random = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			random.normalize();
			random.multiply(0.15);
			
			if (i==0)
				random.multiply(0);
			
			UtilAction.velocity(ent, player.getLocation().getDirection().add(random), 1 + 0.4 * Math.random(), false, 0, 0.2, 10, false);	
			
			Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, 
					null, 1f, 1f, 
					Effect.SMOKE, 4, UpdateType.TICK, 
					0.5f);
		}

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.5f, 0.75f);
		player.getWorld().playSound(player.getLocation(), Sound.SPLASH, 0.75f, 1f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		Explode(data);
		
		if (target == null)
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.PROJECTILE, 3, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		Manager.GetCondition().Factory().Blind(GetName(), target, data.GetThrower(), 1.5, 0, false, false, false);
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		Explode(data);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		Explode(data);
	}

	public void Explode(ProjectileUser data)
	{
		data.GetThrown().getWorld().playSound(data.GetThrown().getLocation(), Sound.EXPLODE, 0.75f, 1.25f);
		data.GetThrown().remove();
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 3);
	}
}
