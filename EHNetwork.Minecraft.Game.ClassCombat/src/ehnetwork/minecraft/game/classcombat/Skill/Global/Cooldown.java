package ehnetwork.minecraft.game.classcombat.Skill.Global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.recharge.RechargeEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Cooldown extends Skill
{
	public Cooldown(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"You quickly recover from using skills;",
				"Skill cooldowns are reduced by #0#12 %.",
				});
	}

	@EventHandler
	public void Resist(RechargeEvent event)
	{
		int level = getLevel(event.GetPlayer());
		if (level <= 0)
			return;
		
		
		double reduction = 0.12f * level;

		event.SetRecharge((long) (event.GetRecharge() * (1 - reduction)));
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
