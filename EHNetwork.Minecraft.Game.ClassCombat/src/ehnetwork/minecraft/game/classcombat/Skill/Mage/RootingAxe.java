package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class RootingAxe extends Skill
{
	public RootingAxe(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your axe rips players downward into",
				"the earth, disrupting their movement."
				});
	}
	
	@Override
	public String GetRechargeString() 
	{
		return "Recharge: #7#-1.5 seconds";
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Root(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		//Damager
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (!UtilGear.isAxe(damager.getItemInHand()))
			return;

		int level = getLevel(damager);
		if (level == 0)			return;
		
		//Damagee
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;
		
		event.SetKnockback(false);

		//Root
		if (Recharge.Instance.use(damager, GetName(), 7000 - (level * 1500), false, false))
			if (UtilEnt.isGrounded(damagee))
			{
				//Double Down
				if (UtilBlock.solid(damagee.getLocation().getBlock()))
					return;
				
				//Fall Through
				if (UtilBlock.airFoliage(damagee.getLocation().getBlock().getRelative(0, -2, 0)))
					return;
				
				//Action
				damagee.teleport(damagee.getLocation().add(0, -0.9, 0));
				
				//Effect
				damagee.getWorld().playEffect(damagee.getLocation().add(0, 1, 0), Effect.STEP_SOUND, damagee.getLocation().getBlock().getTypeId());
			}
		
		//Velocity Down
		UtilAction.velocity(damagee, new Vector(0,-1,0), 0.5, false, 0, 0, 10, false);
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
