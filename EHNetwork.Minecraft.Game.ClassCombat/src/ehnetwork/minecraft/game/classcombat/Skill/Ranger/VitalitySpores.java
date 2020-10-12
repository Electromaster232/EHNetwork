package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class VitalitySpores extends Skill
{
	public VitalitySpores(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"After #5#-1 seconds of not taking damage,",
				"forest spores surround you, restoring",
				"1 health per second.",
				"",
				"This remains until you take damage."
				});
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player cur : GetUsers())
		{ 
			int level = getLevel(cur);
			if (level == 0)		continue;
	
			if (UtilTime.elapsed(Factory.Combat().Get(cur).GetLastDamaged(), 5000 - 1000*level))
			{
				//Factory.Condition().Factory().Regen(GetName(), cur, cur, 3.9 + 2*level, 0, false, true, true);
				UtilPlayer.health(cur, 1);
				
				UtilParticle.PlayParticle(ParticleType.HEART, cur.getEyeLocation().add(UtilAlg.getBehind(cur.getLocation().getDirection().multiply(0.5))), 0, 0.2f, 0, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
			}		
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
