package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MarkedForDeath extends SkillActive
{
	private HashSet<Entity> _arrows = new HashSet<Entity>();
	private HashSet<Player> _active = new HashSet<Player>();
	private HashMap<LivingEntity, Long> _markedTime = new HashMap<LivingEntity, Long>();
	private HashMap<LivingEntity, Double> _markedDamage = new HashMap<LivingEntity, Double>();

	public MarkedForDeath(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
						  int cost, int levels,
						  int energy, int energyMod,
						  long recharge, long rechargeMod, boolean rechargeInform,
						  Material[] itemArray,
						  Action[] actionArray)
	{
		super(skills, name, classType, skillType, 
				cost, levels,
				energy,	energyMod, 
				recharge, rechargeMod, rechargeInform, 
				itemArray,
				actionArray);

		SetDesc(new String[] 
				{
				"Your next arrow will mark players,",
				"making them take #2.5#1.5 more damage",
				"from the next melee attack.",
				"",
				"Lasts for #3#1 seconds."				
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main(GetClassType().name(), "You cannot use " + F.skill(GetName()) + " in water."));
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
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(getLevel(player))) + "."));

		_arrows.add(event.getProjectile());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void DamageMark(CustomDamageEvent event)
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

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.BLAZE_BREATH, 2.5f, 2.0f);

		//Inform
		UtilPlayer.message(event.GetDamageePlayer(), F.main(GetClassType().name(),	F.name(damager.getName()) +" hit you with " + F.skill(GetName(level)) + "."));
		UtilPlayer.message(damager, F.main(GetClassType().name(), "You hit " + F.name(UtilEnt.getName(damagee)) +" with " + F.skill(GetName(level)) + "."));
		
		//Mark
		_markedTime.put(damagee, System.currentTimeMillis() + (3000 + 1000 * level));
		_markedDamage.put(damagee, 2.5 + 1.5 * level);
		
		//Remove
		projectile.remove();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void DamageAmplify(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		if (!_markedTime.containsKey(damagee) || !_markedDamage.containsKey(damagee))
			return;
		
		long time = _markedTime.remove(damagee);
		double damage = _markedDamage.remove(damagee);
		
		if (System.currentTimeMillis() > time)
			return;
		
		event.AddMod(GetName(), GetName(), damage, true);
	}

	@EventHandler
	public void Particle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Entity ent : _arrows)
		{
			UtilParticle.PlayParticle(ParticleType.MOB_SPELL, ent.getLocation(), 0, 0, 0, 0, 1,
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
		_markedTime.remove(player);
		_markedDamage.remove(player);
	}
}
