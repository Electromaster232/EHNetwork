package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class HeavyArrows extends Skill
{
	private HashSet<Entity> _arrows = new HashSet<Entity>();
	
	public HeavyArrows(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Your arrows are extremely heavy,", 
				"moving #5#5 % slower and dealing",
				"an additional #10#10 % knockback",
				"as well as #1#1 additional damage.",
				"",
				"You also receive #10#10 % knockback",
				"when firing arrows if not sneaking.",
				});
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void ShootBow(EntityShootBowEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player)event.getEntity();

		//Level
		int level = getLevel(player);
		if (level == 0)				return;
 
		//Backboost
		if (!player.isSneaking() && Recharge.Instance.use(player, GetName(), 750, false, false))	
		{
			double vel = (event.getProjectile().getVelocity().length() * (0.1 + 0.1 * level));
			UtilAction.velocity(player, player.getLocation().getDirection().multiply(-1), vel, 
					false, 0, 0.3, 0.6, true);
		}

		//Decrease Speed
		event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(0.95 - (level * 0.05)));
		
		_arrows.add(event.getProjectile());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void ArrowHit(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile projectile = event.GetProjectile();
		if (projectile == null)	return;

		if (!_arrows.contains((Entity)projectile))
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		//Knockback
		event.AddKnockback(GetName(), 1.1 + (0.1 * level));
		event.AddMod(GetName(), GetName(), 1 + level, true);
	}
	
	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Entity ent : _arrows)
		{
			UtilParticle.PlayParticle(ParticleType.CRIT, ent.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
		}
	}

	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<Entity> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Entity arrow = arrowIterator.next();
			
			if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround())
				arrowIterator.remove();
		}
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
