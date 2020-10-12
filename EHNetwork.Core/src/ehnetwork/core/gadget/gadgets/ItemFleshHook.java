package ehnetwork.core.gadget.gadgets;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;

public class ItemFleshHook extends ItemGadget implements IThrown
{
	public ItemFleshHook(GadgetManager manager)
	{
		super(manager, "Flesh Hook", new String[]  
				{
				C.cWhite + "Make new friends by throwing a hook",
				C.cWhite + "into their face and pulling them",
				C.cWhite + "towards you!",
				}, 
				-1,  
				Material.getMaterial(131), (byte)0, 
				2000, new Ammo("Flesh Hook", "50 Flesh Hooks", Material.getMaterial(131), (byte)0, new String[] { C.cWhite + "50 Flesh Hooks for you to use!" }, 1000, 50));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(131));
		UtilAction.velocity(item, player.getLocation().getDirection(),
				1.6, false, 0, 0.2, 10, false);

		Manager.getProjectileManager().AddThrow(item, player, this, -1, true, true, true, 
				Sound.FIRE_IGNITE, 1.4f, 0.8f, UtilParticle.ParticleType.CRIT, null, 0, UpdateType.TICK, 0.5f);

		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));

		//Effect
		item.getWorld().playSound(item.getLocation(), Sound.IRONGOLEM_THROW, 2f, 0.8f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		data.GetThrown().remove();

		if (!(data.GetThrower() instanceof Player))
			return;

		Player player = (Player)data.GetThrower();

		if (target == null)
			return;
		
		if (target instanceof Player)
			if (Manager.collideEvent(this, (Player) target))
				return;

		//Pull
		UtilAction.velocity(target, 
				UtilAlg.getTrajectory(target.getLocation(), player.getLocation()),
				3, false, 0, 0.8, 1.5, true);

		//Effect
		target.playEffect(EntityEffect.HURT);

		//Inform
		UtilPlayer.message(target, F.main("Skill", F.name(player.getName()) + " hit you with " + F.skill(GetName()) + "."));
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		data.GetThrown().remove();
	}
}
