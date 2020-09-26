package ehnetwork.minecraft.game.classcombat.Skill.Brute;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Intimidation extends Skill
{
	public Intimidation(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"You intimidate nearby enemies;",
				"Enemies within #3#3 blocks receive Slow 1.",
				});
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			int level = getLevel(cur);
			if (level == 0)		continue;

			HashMap<Player, Double> targets = UtilPlayer.getInRadius(cur.getLocation(), 3 + (level * 3));
			for (Player other : targets.keySet())
				if (!other.equals(cur))
					if (Factory.Relation().canHurt(cur, other))
						if (getLevel(other) < level)
						{
							double dist = targets.get(other);
							int mult = 0;
							//if (dist <= 1 + level)					mult = 2;
							//else if (dist <= 2 + (2 * level))  		mult = 1;
								
							Factory.Condition().Factory().Slow(GetName(), other, cur, 0.9, mult, false, true, false, true);
						}				
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
