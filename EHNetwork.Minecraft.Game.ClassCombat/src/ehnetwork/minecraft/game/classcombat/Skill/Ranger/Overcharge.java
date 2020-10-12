package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillChargeBow;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Overcharge extends SkillChargeBow
{
	private WeakHashMap<Arrow, Double> _arrows = new WeakHashMap<Arrow, Double>();

	public Overcharge(SkillFactory skills, String name, IPvpClass.ClassType classType,
			SkillType skillType, int cost, int maxLevel)
	{
		super(skills, name, classType, skillType, cost, maxLevel, 
				0.012f, 0.006f, false, true);
	
		SetDesc(new String[] 
				{
				"Charge your bow to deal bonus damage.",
				"",
				GetChargeString(),
				"",
				"Deals up to #1.5#1.5 bonus damage."
				});
	}
	
	@Override
	public void DoSkillCustom(Player player, float charge, Arrow arrow)
	{
		double damage = charge * (getLevel(player));
		_arrows.put(arrow, damage);
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

		if (!_arrows.containsKey(projectile))
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		double damage = _arrows.remove(projectile);
		
		//Damage
		event.AddMod(damager.getName(), GetName(), damage, true);

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.HURT_FLESH, 1f, 0.5f);
	}
	
	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Entity ent : _arrows.keySet())
		{
			UtilParticle.PlayParticle(ParticleType.RED_DUST, ent.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
		}
	}

	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<Arrow> arrowIterator = _arrows.keySet().iterator(); arrowIterator.hasNext();) 
		{
			Arrow arrow = arrowIterator.next();
			
			if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround())
				arrowIterator.remove();
		}
	}
}
