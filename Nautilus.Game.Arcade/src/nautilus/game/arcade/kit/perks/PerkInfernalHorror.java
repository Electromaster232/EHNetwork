package nautilus.game.arcade.kit.perks;

import java.util.HashSet;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.events.PlayerGameRespawnEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkInfernalHorror extends SmashPerk
{
	public HashSet<Player> _active = new HashSet<Player>();

	public PerkInfernalHorror() 
	{
		super("Infernal Horror", new String[] 
				{ 
				C.cGray + "Tranform into " + F.skill("Infernal Horror") + " at 100% Rage.",
				C.cGray + "Charge your Rage by dealing/taking damage."
				});
	}
	
	@Override
	public void addSuperCustom(Player player)
	{
		_active.add(player);
		player.setExp(0.9999f);
	}

	@EventHandler
	public void energyUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;

			player.setExp((float) Math.max(0, player.getExp()-0.001));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void damagerEnergy(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.FIRE_TICK)
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!Kit.HasKit(damager))
			return;

		damager.setExp(Math.min(0.999f, damager.getExp() + (float)(event.GetDamage()/60d)));
		
		activeCheck(damager);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void damageeEnergy(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.FIRE_TICK)
			return;
		
		if (event.GetCause() == DamageCause.VOID)
			return;


		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;

		if (!Kit.HasKit(damagee))
			return;

		damagee.setExp(Math.min(0.999f, damagee.getExp() + (float)(event.GetDamage()/60d)));
				
		activeCheck(damagee);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void damageBoost(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		if (!Kit.HasKit(damager))
			return;
		
		if (!_active.contains(damager))
			return;
		
		event.AddMod(damager.getName(), GetName(), 1, false);
	}

	@EventHandler
	public void check(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
			if (Kit.HasKit(player))
				activeCheck(player);
	}

	public void activeCheck(Player player)
	{
		//Active
		if (_active.contains(player))
		{
			if (!isSuperActive(player))
				player.setExp((float) Math.max(0, player.getExp()-0.005));
			
			if (player.getExp() > 0)
			{
				//Condition
				Manager.GetCondition().Factory().Speed(GetName(), player, player, 0.9, 1, false, false, false);
				
				//Particles
				UtilParticle.PlayParticle(ParticleType.FLAME, player.getLocation().add(0, 1, 0), 0.25f, 0.25f, 0.25f, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
				
				if (Math.random() > 0.9)
					UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0, 1, 0), 0.25f, 0.25f, 0.25f, 0, 1,
							ViewDist.LONG, UtilServer.getPlayers());
			}
			else
			{
				_active.remove(player);
				
				//Inform
				UtilPlayer.message(player, F.main("Skill", "You are no longer " + F.skill("Infernal Horror") + "."));
			}
		}
		//Not Active
		else if (player.getExp() > 0.99)
		{
			_active.add(player);

			//Sound
			player.getWorld().playSound(player.getLocation(), Sound.FIRE, 2f, 1f);
			player.getWorld().playSound(player.getLocation(), Sound.FIRE, 2f, 1f);
			
			//Inform
			UtilPlayer.message(player, F.main("Skill", "You transformed into " + F.skill("Infernal Horror") + "."));
		}
	}

	@EventHandler
	public void clean(PlayerGameRespawnEvent event)
	{
		event.GetPlayer().setExp(0f);
		_active.remove(event.GetPlayer());
	}

	public boolean isActive(Player player) 
	{
		return _active.contains(player);
	}
}
