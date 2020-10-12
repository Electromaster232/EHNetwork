package ehnetwork.minecraft.game.core.condition;

import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffectType;

public class ConditionEffect implements Listener
{
	protected ConditionManager Manager;

	public ConditionEffect(ConditionManager manager) 
	{
		Manager = manager;
		Manager.getPlugin().getServer().getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Invulnerable(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		if (!Manager.IsInvulnerable(ent))
			return;

		//Set Damage
		event.SetCancelled("Invulnerable");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void Cloak(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		if (!Manager.IsCloaked(ent))
			return;

		//Set Damage
		event.SetCancelled("Cloak");
	}

	@EventHandler
	public void Cloak(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (LivingEntity ent : Manager.GetActiveConditions().keySet())
		{
			if (!(ent instanceof Player))
				continue;

			Player player = (Player)ent;

			//Hide
			if (Manager.IsCloaked(ent))
			{
				for (Player other : Bukkit.getServer().getOnlinePlayers())
				{
					VisibilityManager.Instance.setVisibility(player, false, other);
				}
			}
			//Show
			else
			{
				for (Player other : Bukkit.getServer().getOnlinePlayers())
				{
					VisibilityManager.Instance.setVisibility(player, true, other);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void Cloak(EntityTargetEvent event)
	{
		if (!(event.getTarget() instanceof Player))
			return;
		
		if (!Manager.HasCondition((LivingEntity)event.getTarget(), Condition.ConditionType.CLOAK, null))
			return;
		
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Protection(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (!damagee.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
			return;

		Condition cond = Manager.GetActiveCondition(damagee, Condition.ConditionType.DAMAGE_RESISTANCE);
		if (cond == null)		return;

		event.AddMod(UtilEnt.getName(cond.GetSource()), cond.GetReason(), -1 * (cond.GetMult()+1), false);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void VulnerabilityDamagee(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (!damagee.hasPotionEffect(PotionEffectType.WITHER))
			return;

		Condition cond = Manager.GetActiveCondition(damagee, Condition.ConditionType.WITHER);
		if (cond == null)		return;

		event.AddMod(UtilEnt.getName(cond.GetSource()), cond.GetReason(), cond.GetMult()+1, false);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void VulnerabilityDamager(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (!damager.hasPotionEffect(PotionEffectType.WITHER))
			return;

		Condition cond = Manager.GetActiveCondition(damager, Condition.ConditionType.WITHER);
		if (cond == null)		return;

		event.AddMod(UtilEnt.getName(cond.GetSource()), cond.GetReason(), -1 * (cond.GetMult()+1), false);
	}

	@EventHandler
	public void VulnerabilityEffect(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (LivingEntity ent : Manager.GetActiveConditions().keySet())
		{
			if (ent.isDead())
				continue;
			
			if (!ent.hasPotionEffect(PotionEffectType.WITHER))
				continue;
			
			if (Manager.HasCondition(ent, Condition.ConditionType.CLOAK, null))
				continue;
			
			ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 1);
			ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 3);
			ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 5);
			ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 7);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void VulnerabilityWitherCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.WITHER)
			event.SetCancelled("Vulnerability Wither");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Strength(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
			return;

		Condition cond = Manager.GetActiveCondition(damager, Condition.ConditionType.INCREASE_DAMAGE);
		if (cond == null)		return;

		event.AddMod(damager.getName(), cond.GetReason(), cond.GetMult() + 1, true);
	}

	@EventHandler
	public void Shock(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (LivingEntity ent : Manager.GetActiveConditions().keySet())
			for (ConditionActive ind : Manager.GetActiveConditions().get(ent))
				if (ind.GetCondition().GetType() == Condition.ConditionType.SHOCK)
					ent.playEffect(EntityEffect.HURT);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Lightning(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.LIGHTNING)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.LIGHTNING);
		if (condition == null)	return;

		//Damage
		event.SetDamager(condition.GetSource());
		event.AddMod(UtilEnt.getName(condition.GetSource()), condition.GetReason(), 0, true);

		if (condition.GetMult() != 0)
			event.AddMod("Lightning Modifier", UtilEnt.getName(condition.GetSource()), condition.GetMult(), false);

		event.SetKnockback(false);
	}

	@EventHandler
	public void Explosion(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_EXPLOSION && event.GetCause() != DamageCause.BLOCK_EXPLOSION)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.EXPLOSION);
		if (condition == null)	return;

		//Damage
		event.SetDamager(condition.GetSource());

		event.AddMod("Negate", condition.GetReason(), -event.GetDamageInitial(), false);
		event.AddMod(UtilEnt.getName(condition.GetSource()), condition.GetReason(), Math.min(event.GetDamageInitial(), condition.GetMult()), true);

		event.SetKnockback(false);
	}

	@EventHandler
	public void Fire(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.FIRE_TICK)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		//Limit
		if (ent.getFireTicks() > 160)
			ent.setFireTicks(160);

		Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.BURNING);
		if (condition == null)	return;

		//Damage
		event.SetDamager(condition.GetSource());
		event.AddMod(UtilEnt.getName(condition.GetSource()), condition.GetReason(), 0, true);
		event.SetIgnoreArmor(true);
		event.SetKnockback(false);
	}

	@EventHandler
	public void FireDouse(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (LivingEntity ent : Manager.GetActiveConditions().keySet())
			if (ent.getFireTicks() <= 0)
				Manager.EndCondition(ent, Condition.ConditionType.BURNING, null);
	}

	@EventHandler
	public void Poison(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.POISON)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.POISON);
		if (condition == null)	return;

		//Damage
		event.SetDamager(condition.GetSource());
		event.AddMod(UtilEnt.getName(condition.GetSource()), condition.GetReason(), 0, true);
		event.SetIgnoreArmor(true);
		event.SetKnockback(false);
	}

	@EventHandler
	public void Fall(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.FALL)
			return;

		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;

		Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.FALLING);
		if (condition == null)	return;

		//Damage
		event.SetDamager(condition.GetSource());
		event.AddMod(UtilEnt.getName(condition.GetSource()), condition.GetReason(), 0, true);
		event.SetIgnoreArmor(true);
		event.SetKnockback(false);
	}

	@EventHandler
	public void Fall(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (LivingEntity ent : Manager.GetActiveConditions().keySet())
		{
			if (!UtilEnt.isGrounded(ent))
				continue;

			Condition condition = Manager.GetActiveCondition(ent, Condition.ConditionType.FALLING);
			if (condition == null)	return;

			if (!UtilTime.elapsed(condition.GetTime(), 250))
				continue;

			Manager.EndCondition(ent, Condition.ConditionType.FALLING , null);
		}
	}
}
