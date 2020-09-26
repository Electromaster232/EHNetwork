package ehnetwork.minecraft.game.classcombat.Skill.Global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Rations extends Skill
{
	public Rations(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"After not moving for #4#-1 seconds,",
				"you snack on rations, replenishing",
				"#0#1 hunger every second."
				});
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player cur : UtilServer.getPlayers())
		{
			int level = getLevel(cur);
			
			if (level > 0)
			{
				if (UtilTime.elapsed(Factory.Movement().Get(cur).LastMovement, 7000 - (1000 * level)))
				{
					cur.setSaturation(0f);
					cur.setExhaustion(0f);
					UtilPlayer.hunger(cur, level);	
				}
			}
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
