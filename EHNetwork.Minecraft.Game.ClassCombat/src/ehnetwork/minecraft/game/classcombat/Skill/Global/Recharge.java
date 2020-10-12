package ehnetwork.minecraft.game.classcombat.Skill.Global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.energy.event.EnergyEvent;
import ehnetwork.core.energy.event.EnergyEvent.EnergyChangeReason;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Recharge extends Skill
{
	public Recharge(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"For every second since you",
				"last used Energy, you receive",
				"+ #0#20 % Energy regenerate rate.",
				"",
				"Maximum of + #0#100 % bonus."
				});
	}
	
	@EventHandler
	public void Skill(EnergyEvent event)
	{
		if (event.GetReason() != EnergyChangeReason.Recharge)
			return;
		
		int level = getLevel(event.GetPlayer());
		if (level <= 0)
			return;
		
		long duration = System.currentTimeMillis() - Factory.Energy().Get(event.GetPlayer()).LastEnergy;
		
		int bonus = (int) (duration / 1000);
		
		if (bonus > 5)
			bonus = 5;
		
		event.AddMod(bonus * (level * 0.08));
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
