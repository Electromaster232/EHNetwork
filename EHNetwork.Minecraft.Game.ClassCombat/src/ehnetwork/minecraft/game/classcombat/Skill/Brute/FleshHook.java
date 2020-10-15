package ehnetwork.minecraft.game.classcombat.Skill.Brute;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActiveCharge;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillEvent;

public class FleshHook extends SkillActiveCharge implements IThrown
{
	public FleshHook(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
					 int cost, int levels,
					 int energy, int energyMod,
					 long recharge, long rechargeMod, boolean rechargeInform,
					 Material[] itemArray,
					 Action[] actionArray)
	{
		super(skills, name, classType, skillType,
				cost, levels,
				energy, energyMod, 
				recharge, rechargeMod, rechargeInform, 
				itemArray,
				actionArray,
				0.01f, 0.005f);

		SetDesc(new String[] 
				{
				"Hold Block to charge Flesh Hook.",
				"Release Block to release it.",
				"",
				GetChargeString(),
				"",
				"If Flesh Hook hits a player, it",
				"deals up to #2#2 damage, and rips them",
				"towards you with #1.2#0.2 velocity.",
				});
	}
	
	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Start Charge 
		_charge.put(player, 0f);
	}

	@EventHandler
	public void ChargeRelease(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			//Not Charging
			if (!_charge.containsKey(cur))
				continue;

			//Level
			int level = getLevel(cur);
			if (level == 0)			return;

			//Add Charge
			if (cur.isBlocking())
			{
				Charge(cur);
			}

			//Release Charge
			else if (_charge.containsKey(cur))
			{
				float charge = _charge.remove(cur);

				//Action
				Item item = cur.getWorld().dropItem(cur.getEyeLocation().add(cur.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(131));
				UtilAction.velocity(item, cur.getLocation().getDirection(), 
						1 + charge , false, 0, 0.2, 20, false);
				 
				Factory.Projectile().AddThrow(item, cur, this, -1, true, true, true, 
						Sound.FIRE_IGNITE, 1.4f, 0.8f, ParticleType.CRIT, UpdateType.TICK, 0.6f);

				//Inform
				UtilPlayer.message(cur, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));

				//Effect
				item.getWorld().playSound(item.getLocation(), Sound.IRONGOLEM_THROW, 2f, 0.8f);
				
			}	
		}
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

		//Level
		int level = getLevel(player);
		if (level == 0)				return;

		if (target == null)
			return;

		//Pull
		UtilAction.velocity(target, 
				UtilAlg.getTrajectory(target.getLocation(), player.getLocation()), 
				1.2 + (0.2 * level), false, 0, 0.8, 1.5, true);
		
		//Condition
		Factory.Condition().Factory().Falling(GetName(), target, player, 10, false, true);

		//Damage Event
		Factory.Damage().NewDamageEvent(target, player, null, target.getLocation(),
				DamageCause.CUSTOM, velocity * (1 + (1*level)), false, true, false,
				player.getName(), GetName());
		

		//Event
		if (target != null)
			UtilServer.getServer().getPluginManager().callEvent(new SkillEvent(player, GetName(), IPvpClass.ClassType.Brute, target));
		
		//Inform
		UtilPlayer.message(target, F.main(GetClassType().name(), F.name(player.getName()) + " pulled you with " + F.skill(GetName(level)) + "."));
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
