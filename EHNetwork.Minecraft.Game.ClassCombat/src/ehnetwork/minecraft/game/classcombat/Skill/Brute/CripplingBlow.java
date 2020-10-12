package ehnetwork.minecraft.game.classcombat.Skill.Brute;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class CripplingBlow extends Skill
{
	public CripplingBlow(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your powerful axe attacks give",
				"targets Slow 2 for 1.5 second,",
				"as well as no knockback."
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		//Damager
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		//Maul
		if (!UtilGear.isAxe(damager.getItemInHand()))
			return;

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		//Stun
		Factory.Condition().Factory().Slow(GetName(), damagee, damager, 0.5 + 0.5 * 1, 1, false, true, false, true);

		//Damage
		event.AddMod(damager.getName(), GetName(), 0, true);
		event.SetKnockback(false);
		
		//Event
		UtilServer.getServer().getPluginManager().callEvent(new SkillEvent(damager, GetName(), IPvpClass.ClassType.Brute, damagee));
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
