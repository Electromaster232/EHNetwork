package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.Perk;

public class PerkDeathsGrasp extends Perk
{
	private HashMap<LivingEntity, Long> _live = new HashMap<LivingEntity, Long>();
	private HashMap<LivingEntity, Long> _weakness = new HashMap<LivingEntity, Long>();

	public PerkDeathsGrasp() 
	{
		super("Deaths Grasp", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Deaths Grasp",
				C.cGray + "+100% Arrow Damage to enemies thrown by Deaths Grasp"
				});
	}

	@EventHandler
	public void leap(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (!UtilGear.isBow(event.getPlayer().getItemInHand()))
			return; 

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 12000, true, true))
			return;

		UtilAction.velocity(player, player.getLocation().getDirection(), 1.4, false, 0, 0.2, 1.2, true);

		//Record
		_live.put(player, System.currentTimeMillis());

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
		
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_HURT, 1f, 1.4f);
	}

	@EventHandler
	public void end(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Collide
		for (Player player : Manager.GetGame().GetPlayers(true))
			if (_live.containsKey(player))
				for (Player other : Manager.GetGame().GetPlayers(true))
					if (!Manager.isSpectator(other))
						if (!other.equals(player))
							if (UtilMath.offset(player, other) < 2)
							{
								collide(player, other);
								_live.remove(player);
								return;
							}

		//Leap End
		Iterator<LivingEntity> leapIter = _live.keySet().iterator();
		
		while (leapIter.hasNext())
		{
			LivingEntity ent = leapIter.next();
			
			if (!UtilEnt.isGrounded(ent))
				continue;
			
			if (!UtilTime.elapsed(_live.get(ent), 1000))  
				continue;
			
			leapIter.remove();
		}

		//Weakness End
		Iterator<LivingEntity> weaknessIter = _weakness.keySet().iterator();
		
		while (weaknessIter.hasNext())
		{
			LivingEntity ent = weaknessIter.next();
			
			if (!UtilEnt.isGrounded(ent))
				continue;
			
			if (!UtilTime.elapsed(_weakness.get(ent), 1000))  
				continue;
			
			_weakness.remove(ent);
		}
	}
	
	public void collide(Player damager, LivingEntity damagee)
	{
		//Damage Event
		Manager.GetDamage().NewDamageEvent(damagee, damager, null, 
				DamageCause.CUSTOM, 6, false, true, false,
				damager.getName(), GetName());	
		
		UtilAction.velocity(damagee, UtilAlg.getTrajectory(damagee, damager), 1.8, false, 0, 1, 1.8, true);
		
		damager.setVelocity(new Vector(0,0,0));
		
		damager.getWorld().playSound(damager.getLocation(), Sound.ZOMBIE_HURT, 1f, 0.7f);
		
		_weakness.put(damagee, System.currentTimeMillis());

		//Inform
		UtilPlayer.message(damager, F.main("Game", "You hit " + F.name(UtilEnt.getName(damagee)) + " with " + F.skill(GetName()) + "."));
		UtilPlayer.message(damagee, F.main("Game", F.name(damager.getName()) + " hit you with " + F.skill(GetName()) + "."));
		
		Recharge.Instance.recharge(damager, GetName());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void arrowDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		if (!(event.GetProjectile() instanceof Arrow))
			return;
		
		if (!_weakness.containsKey(event.GetDamageeEntity()))
			return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;

		event.AddMult(GetName(), GetName() + " Combo", 2, true);
		
		UtilParticle.PlayParticle(ParticleType.RED_DUST, event.GetDamageeEntity().getLocation(), 0.5f, 0.5f, 0.5f, 0, 20,
				ViewDist.MAX, UtilServer.getPlayers());
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, event.GetDamageeEntity().getLocation(), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		damager.getWorld().playSound(damager.getLocation(), Sound.ZOMBIE_HURT, 1f, 2f);
	}
}
