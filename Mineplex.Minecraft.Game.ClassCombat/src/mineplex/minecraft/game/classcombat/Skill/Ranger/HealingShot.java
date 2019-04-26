package mineplex.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import mineplex.minecraft.game.classcombat.Class.IPvpClass.ClassType;
import mineplex.core.common.util.F;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.updater.UpdateType;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.minecraft.game.classcombat.Skill.SkillActive;
import mineplex.minecraft.game.classcombat.Skill.SkillFactory;

public class HealingShot extends SkillActive
{
	private HashSet<Entity> _arrows = new HashSet<Entity>();
	private HashSet<Player> _active = new HashSet<Player>();

	public HealingShot(SkillFactory skills, String name, ClassType classType, SkillType skillType, 
			int cost, int levels, 
			int energy, int energyMod, 
			long recharge, long rechargeMod, boolean rechargeInform, 
			Material[] itemArray, 
			Action[] actionArray) 
	{
		super(skills, name, classType, skillType,
				cost, levels,
				energy, energyMod, 
				recharge, rechargeMod, rechargeInform, 
				itemArray,
				actionArray);

		SetDesc(new String[] 
				{
				"Prepare a healing shot;",
				"Your next arrow will give its target",
				"Regeneration 2 for #5#1 seconds,",
				"and remove all negative effects."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Action
		_active.add(player);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You prepared " + F.skill(GetName(level)) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 2.5f, 2.0f);
	}

	@EventHandler
	public void ShootBow(EntityShootBowEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;

		if (!(event.getProjectile() instanceof Arrow))
			return;

		Player player = (Player)event.getEntity();

		if (!_active.remove(player))
			return;

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You fired " + F.skill(GetName(getLevel(player))) + "."));

		_arrows.add(event.getProjectile());
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void ArrowHit(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.PROJECTILE)
			return;
		
		if (!(event instanceof EntityDamageByEntityEvent))
			return;
	
		EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;
		
		if (!(eventEE.getDamager() instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile)eventEE.getDamager();

		//Not Pin Down Arrow
		if (!_arrows.contains((Entity)projectile))
			return;
		
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		LivingEntity damagee = (LivingEntity)event.getEntity();

		if (projectile.getShooter() == null)
			return;
		
		if (!(projectile.getShooter() instanceof Player))
			return;
		
		Player damager = (Player)projectile.getShooter();

		//Level
		int level = getLevel(damager);
		if (level == 0)			return;

		//Remove
		_arrows.remove(projectile);
		projectile.remove();

		//Regen
		Factory.Condition().Factory().Regen(GetName(), damagee, damager, 3 + 2 * level, 1, false, true, true);

		//Remove Bad
		damagee.setFireTicks(0);
		damagee.removePotionEffect(PotionEffectType.SLOW);
		damagee.removePotionEffect(PotionEffectType.POISON);
		damagee.removePotionEffect(PotionEffectType.CONFUSION);
		damagee.removePotionEffect(PotionEffectType.WEAKNESS);

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.LEVEL_UP, 1f, 1.5f);
		damagee.getWorld().playEffect(damagee.getLocation(), Effect.STEP_SOUND, 115);
		damagee.playEffect(EntityEffect.HURT);

		//Inform
		UtilPlayer.message(damagee, F.main(GetClassType().name(), 
				F.name(damager.getName()) +" hit you with " + F.skill(GetName(level)) + "."));

		UtilPlayer.message(damager, F.main(GetClassType().name(), 
				"You hit " + F.name(UtilEnt.getName(damagee)) +" with " + F.skill(GetName(level)) + "."));
		
		//Particles
		UtilParticle.PlayParticle(ParticleType.HEART, damagee.getLocation(), 
				(float)(Math.random() - 0.5), (float)(Math.random() + 0.5), (float)(Math.random() - 0.5), 2f, 12,
				ViewDist.MAX, UtilServer.getPlayers());
		
		//Remove
		projectile.remove();
	}
	
	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Entity ent : _arrows)
		{
			UtilParticle.PlayParticle(ParticleType.HEART, ent.getLocation(), 0, 0, 0, 0, 1,
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
		_active.remove(player);
	}
}
