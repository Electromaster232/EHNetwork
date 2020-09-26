package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkSnowTurret extends SmashPerk
{
	private WeakHashMap<Projectile, Player> _snowball = new WeakHashMap<Projectile, Player>();

	private HashMap<Snowman, Player> _turret = new HashMap<Snowman, Player>();

	public PerkSnowTurret() 
	{
		super("Snow Turret", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{
		Manager.GetGame().CreatureAllowOverride = true;
		Snowman ent = player.getWorld().spawn(player.getEyeLocation(), Snowman.class);
		Manager.GetGame().CreatureAllowOverride = false;

		UtilEnt.Vegetate(ent);
		UtilEnt.ghost(ent, true, false);

		ent.setMaxHealth(40);
		ent.setHealth(40);

		UtilAction.velocity(ent, player.getLocation().getDirection(), 1, false, 0, 0.2, 1, false);

		_turret.put(ent, player);
	}

	@EventHandler
	public void updateSnowman(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		Iterator<Snowman> turretIter = _turret.keySet().iterator();

		while (turretIter.hasNext())
		{
			Snowman snowman = turretIter.next();
			Player player = _turret.get(snowman);

			if (snowman.getTicksLived() > 400)
			{
				UtilParticle.PlayParticle(ParticleType.SNOWBALL_POOF, snowman.getLocation().add(0, 1, 0), 0.4f, 0.4f, 0.4f, 0, 12,
						ViewDist.LONG, UtilServer.getPlayers());
				turretIter.remove();
				snowman.remove();
				continue;
			}

			Player target = UtilPlayer.getClosest(snowman.getLocation(), player);
			if (target == null)
				continue;

			snowman.setTarget(target);

			//Snowball
			double mult = 1 + Math.min(3, UtilMath.offset(snowman, target)/16);
			double heightBonus = UtilMath.offset(snowman, target)/140;
			Vector rand = new Vector((Math.random()-0.5)*0.2,(Math.random()-0.5)*0.2,(Math.random()-0.5)*0.2);

			_snowball.put(snowman.launchProjectile(Snowball.class, UtilAlg.getTrajectory(snowman.getLocation(), 
					target.getLocation()).multiply(mult).add(rand).add(new Vector(0,heightBonus,0))), player);

			//Look dir
			UtilEnt.CreatureMoveFast(snowman, target.getLocation(), 0.1f);

			//Sound
			snowman.getWorld().playSound(snowman.getLocation(), Sound.STEP_SNOW, 0.6f, 1f);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void snowballHit(CustomDamageEvent event)
	{		
		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile proj = event.GetProjectile();
		if (proj == null)		return;

		if (!(proj instanceof Snowball))
			return;

		if (!_snowball.containsKey(proj))
			return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		event.SetCancelled("Turret");

		if (damagee.equals(_snowball.get(proj)))
			return;

		damagee.setVelocity(proj.getVelocity().multiply(0.3).add(new Vector(0, 0.3, 0)));

		//Damage Event
		if (!(damagee instanceof LivingEntity))
			return;

		if (!Recharge.Instance.use((Player)damagee, GetName() + " Hit", 250, false, false))
			return;

		Manager.GetDamage().NewDamageEvent(damagee, _snowball.get(proj), null, 
				DamageCause.PROJECTILE, 2, false, true, false,
				UtilEnt.getName(_snowball.get(proj)), GetName());
	}

	@EventHandler
	public void damageCancel(CustomDamageEvent event)
	{
		if (_turret.containsKey(event.GetDamageeEntity()))
			event.SetCancelled("Turret Immunity");
	}

	@EventHandler
	public void clean(ProjectileHitEvent event)
	{
		_snowball.remove(event.getEntity());
	}

	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 2.5);
	}
}
