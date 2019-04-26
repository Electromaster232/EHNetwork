package mineplex.minecraft.game.classcombat.Skill.Knight;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.core.common.util.UtilPlayer;
import mineplex.minecraft.game.classcombat.Skill.Skill;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class LevelField extends Skill
{
	public LevelField(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				/*
				"For every enemy within #4#2 blocks",
				"that is closer than your nearest ally,",
				"you deal 1 more damage and take 1 less.",
				"",
				"Damage can be altered a maximum of #1#1.",
				*/
				
				"Even the battlefield with courage!",
				"You deal X more damage.",
				"You take X less damage.",
				"X = (Nearby Enemies) - (Nearby Allies)",
				"Players within #4#2 Blocks are considered.",
				"",
				"Damage can be altered a maximum of #1#1.",
				"",
				"You can not deal less damage, or take",
				"more damage via this."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Decrease(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		//Damager
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		LivingEntity damager = event.GetDamagerEntity(false);
		if (damager == null)	return;

		//Level
		int level = getLevel(damagee);
		if (level == 0)			return;

		int alt = 0;

		for (Player cur : UtilPlayer.getNearby(damagee.getLocation(), 4 + (2 * level)))
		{
			if (cur.equals(damagee))
				alt += 1;

			else if (Factory.Relation().canHurt(damagee, cur))
				alt -= 1;

			else
				alt += 1;
		}
		
		int limit = 1 + level;

		if (alt > limit)		alt = limit;
		if (alt < -limit)		alt = -limit;

		if (alt >= 0)
			return;
		
		//Decrease Damage
		event.AddMod(damagee.getName(), GetName(), alt, false);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Increase(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		//Damager
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		int alt = 0;

		for (Player cur : UtilPlayer.getNearby(damager.getLocation(), 4 + (2 * level)))
		{
			if (cur.equals(damager))
				alt -= 1;

			else if (Factory.Relation().canHurt(damager, cur))
				alt += 1;

			else
				alt -= 1;
		}
		
		int limit = 1 + level;

		if (alt > limit)	alt = limit;
		if (alt < -limit)	alt = -limit;
		
		if (alt <= 0)
			return;

		//Decrease Damage
		event.AddMod(damager.getName(), GetName(), alt, false);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
