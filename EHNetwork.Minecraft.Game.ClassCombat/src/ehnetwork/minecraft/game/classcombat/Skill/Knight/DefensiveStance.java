package ehnetwork.minecraft.game.classcombat.Skill.Knight;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DefensiveStance extends SkillActive
{
	private HashMap<Player, Long> _useTime = new HashMap<Player, Long>();
	
	public DefensiveStance(SkillFactory skills, String name, ClassType classType, SkillType skillType, 
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
				actionArray);

		SetDesc(new String[] 
				{
				"While Blocking, you take 75% less",
				"damage from attacks infront of you."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		_useTime.put(player, System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void AntiTurtle(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK && event.GetCause() != DamageCause.PROJECTILE)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (!_useTime.containsKey(damager))
			return;
		
		if (UtilTime.elapsed(_useTime.get(damager), 400))
			return;
		
		event.SetCancelled(GetName() + " Attack");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damagee(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK && event.GetCause() != DamageCause.PROJECTILE)
			return;
		
		if (UtilEnt.inWater(event.GetDamageeEntity()))
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (!damagee.isBlocking())
			return;
		
		int level = getLevel(damagee);
		if (level == 0)		return;

		LivingEntity damager = event.GetDamagerEntity(true);
		if (damager == null)	return;

		Vector look = damagee.getLocation().getDirection();
		look.setY(0);
		look.normalize();

		Vector from = UtilAlg.getTrajectory(damagee, damager);
		from.normalize();

		if (damagee.getLocation().getDirection().subtract(from).length() > 1.4)
		{
			//event.AddMod(damagee.getName(), GetName(), -0.5 - (0.5 * level), true);
			return;
		}

		//Damage
		event.AddMult(GetName(), GetName(), 0.25, false);

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.ZOMBIE_METAL, 1f, 2f);
	}

	@Override
	public void Reset(Player player) 
	{
		_useTime.remove(player);
	}
}
