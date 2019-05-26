package nautilus.game.arcade.kit.perks;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;

import nautilus.game.arcade.kit.Perk;

public class PerkIronHook extends Perk implements IThrown
{
	public PerkIronHook() 
	{
		super("Iron Hook", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Pickaxe to " + C.cGreen + "Iron Hook"
				});
	}

	@EventHandler
	public void Activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_PICKAXE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 8000, true, true))
			return;

		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(131));
		UtilAction.velocity(item, player.getLocation().getDirection(), 
				1.8, false, 0, 0.2, 10, false);

		Manager.GetProjectile().AddThrow(item, player, this, -1, true, true, true, 
				Sound.FIRE_IGNITE, 1.4f, 0.8f, ParticleType.CRIT, null, 0, UpdateType.TICK, 0.6f);

		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));

		//Effect
		item.getWorld().playSound(item.getLocation(), Sound.IRONGOLEM_THROW, 2f, 0.8f);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{ 
		//Remove
		double velocity = data.GetThrown().getVelocity().length();
		data.GetThrown().remove();

		if (!(data.GetThrower() instanceof Player))
			return;

		Player player = (Player)data.GetThrower();

		if (target == null)
			return;

		//Pull
		UtilAction.velocity(target, 
				UtilAlg.getTrajectory(target.getLocation(), player.getLocation()), 
				2, false, 0, 0.8, 1.5, true);

		//Condition
		Manager.GetCondition().Factory().Falling(GetName(), target, player, 10, false, true);

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, player, null, 
				DamageCause.CUSTOM, velocity * 4, false, true, false,
				player.getName(), GetName());

		//Inform
		UtilPlayer.message(target, F.main("Skill", F.name(player.getName()) + " hit you with " + F.skill(GetName()) + "."));
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		//Remove
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		//Remove
		data.GetThrown().remove();
	}

}
