package mineplex.minecraft.game.classcombat.Skill.Global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import mineplex.minecraft.game.core.condition.Condition.ConditionType;
import mineplex.minecraft.game.core.condition.events.ConditionApplyEvent;
import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.minecraft.game.classcombat.Skill.Skill;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class Resistance extends Skill
{
	public Resistance(SkillFactory skills, String name, ClassType classType, SkillType skillType, int cost, int levels) 
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your body and mind is exceptionally resistant.",
				"Durations on you are #0#25 % shorter for;",
				"Slow, Fire, Shock, Confusion, Poison, Blindness."
				});
	}

	@EventHandler
	public void Resist(ConditionApplyEvent event)
	{
		if (event.GetCondition().GetType() != ConditionType.BURNING &&
			event.GetCondition().GetType() != ConditionType.SLOW &&
			event.GetCondition().GetType() != ConditionType.SHOCK &&
			event.GetCondition().GetType() != ConditionType.CONFUSION &&
			event.GetCondition().GetType() != ConditionType.POISON &&
			event.GetCondition().GetType() != ConditionType.BLINDNESS)
			return;
		
		//Dont Resist Self Condition
		if (event.GetCondition().GetReason().equalsIgnoreCase("Hold Position"))
		{
			return;
		}
		
		
		int level = getLevel(event.GetCondition().GetEnt());
		if (level <= 0)
			return;
				
		double reduction = -(0.25f * level);

		event.GetCondition().ModifyTicks((int) (event.GetCondition().GetTicksTotal() * reduction));
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
