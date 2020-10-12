package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class BarbedArrows extends Skill
{
	private HashSet<Projectile> _arrows = new HashSet<Projectile>();
	
	public BarbedArrows(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your arrows are barbed, and give", 
				"opponents Slow 1 for #2#1 seconds.",
				"Will cancel sprint on opponents.",
				"",
				"Duration scales with arrow velocity."
				});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void bowShoot(EntityShootBowEvent event)
	{
		int level = getLevel(event.getEntity());
		if (level == 0)				
			return;
		
		if (!(event.getProjectile() instanceof Projectile))
			return;
		
		_arrows.add((Projectile)event.getProjectile());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile projectile = event.GetProjectile();
		if (projectile == null)	return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		//Level
		int level = getLevel(damager);
		if (level == 0)				return;

		Player damageePlayer = event.GetDamageePlayer();

		if (damageePlayer != null)
			damageePlayer.setSprinting(false);

		//Damage
		event.AddMod(damager.getName(), GetName(), 0, false);

		//Condition
		Factory.Condition().Factory().Slow(GetName(), damagee, damager, (projectile.getVelocity().length() / 3) * (2 + level), 0, false, true, true, true);
	}
	
	@EventHandler
	public void clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<Projectile> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Projectile arrow = arrowIterator.next();
			
			if (arrow.isDead() || !arrow.isValid())
				arrowIterator.remove();
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
