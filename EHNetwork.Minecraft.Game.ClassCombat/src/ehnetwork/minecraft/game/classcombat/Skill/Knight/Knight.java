package ehnetwork.minecraft.game.classcombat.Skill.Knight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.energy.event.EnergyEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Knight extends Skill
{
	public Knight(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
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

	@EventHandler
	public void CancelEnergy(EnergyEvent event)
	{
		if (getLevel(event.GetPlayer()) > 0)
			event.setCancelled(true);
	}
	
	@Override
	public void Reset(Player player)
	{

	}
}
