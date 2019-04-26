package mineplex.minecraft.game.classcombat.Skill.Mage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.classcombat.Skill.Skill;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class Mage extends Skill
{
	public Mage(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"25% reduction in Arrow Velocity."
				});
	}
	
	@EventHandler
	public void BowShoot(EntityShootBowEvent event)
	{
		if (getLevel(event.getEntity()) == 0)
			return;
		
		event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(0.75));
	}

	@Override
	public void Reset(Player player)
	{
		
	}
}
