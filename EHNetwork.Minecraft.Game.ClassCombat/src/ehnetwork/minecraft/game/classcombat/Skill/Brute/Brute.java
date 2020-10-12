package ehnetwork.minecraft.game.classcombat.Skill.Brute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.energy.event.EnergyEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Brute extends Skill
{
	public Brute(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
					"You take 8 more damage from enemy attacks",
					"to counter the strength of Diamond Armor.",
					"",
					"25% reduction in Arrow Velocity."
				});
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		int level = getLevel(damagee);
		if (level == 0)		return;

		//Damage
		event.AddMod(damagee.getName(), GetName(), 0, false);
		event.SetBrute();
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
