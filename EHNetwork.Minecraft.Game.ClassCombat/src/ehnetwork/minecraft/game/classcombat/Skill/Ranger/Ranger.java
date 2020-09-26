package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.energy.event.EnergyEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Ranger extends Skill
{
	public Ranger(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				
				});
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
