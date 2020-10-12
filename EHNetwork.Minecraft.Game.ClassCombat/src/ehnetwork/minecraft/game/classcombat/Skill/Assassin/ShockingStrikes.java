package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

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

public class ShockingStrikes extends Skill
{
	public ShockingStrikes(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your attacks shock targets for", 
				"#0#1 seconds, giving them Slow 1 and",
				"Screen-Shake."
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
		Factory.Condition().Factory().Shock(GetName(), damagee, damager, level, false, false);
		Factory.Condition().Factory().Slow(GetName(), damagee, damager, level, 0, false, false, true, false);

		//Damage
		event.AddMod(damager.getName(), GetName(), 0, true);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
