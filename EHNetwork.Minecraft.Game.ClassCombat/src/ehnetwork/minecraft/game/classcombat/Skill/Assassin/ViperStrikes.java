package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilGear;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ViperStrikes extends Skill
{
	public ViperStrikes(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your attacks give enemies",
				"Poison 1 for #1#2 seconds."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		if (!UtilGear.isWeapon(damager.getItemInHand()))
			return;

		int level = getLevel(damager);
		if (level == 0)		return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		//Confuse
		Factory.Condition().Factory().Poison(GetName(), damagee, damager, 1 + 2*level, 0, false, false, false);

		//Damage
		//event.AddMod(damager.getName(), GetName(), -1 - level, true);
		
		//Sound
		damager.getWorld().playSound(damager.getLocation(), Sound.SPIDER_IDLE, 1f, 2f);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
