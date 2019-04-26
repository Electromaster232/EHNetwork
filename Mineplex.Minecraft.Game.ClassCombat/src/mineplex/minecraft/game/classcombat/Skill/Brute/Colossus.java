package mineplex.minecraft.game.classcombat.Skill.Brute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import mineplex.minecraft.game.classcombat.Skill.Skill;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class Colossus extends Skill
{
	public Colossus(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"You are so huge that you take",
				"#15#20 % less knockback from attacks."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		int level = getLevel(damagee);
		if (level == 0)		return;

		//Damage
		event.AddMod(damagee.getName(), GetName(), 0, false);
		event.AddKnockback(GetName(), 0.85 - 0.20*level);
	}

	@Override
	public void Reset(Player player)
	{

	}
}
