package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseCreeper;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkCreeperElectricity extends Perk
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();
	
	public PerkCreeperElectricity() 
	{
		super("Lightning Shield", new String[] 
				{
				"When hit by a non-melee attack, you gain " + C.cGreen + "Lightning Shield"
				});
	}


	@EventHandler
	public void Shield(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() == DamageCause.ENTITY_ATTACK)
			return;
		
		if (event.GetCause() == DamageCause.FIRE_TICK)
			return;
		
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)		return;
		
		if (!Kit.HasKit(damagee))
			return;
		
		_active.put(damagee, System.currentTimeMillis());
		
		SetPowered(damagee, true);
		
		//Sound
		damagee.getWorld().playSound(damagee.getLocation(), Sound.CREEPER_HISS, 3f, 1.25f);
		
		//Inform
		UtilPlayer.message(damagee, F.main("Skill", "You gained " + F.skill(GetName()) + "."));
	}

	
	@EventHandler
	public void Update(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> shieldIterator = _active.keySet().iterator();

		while (shieldIterator.hasNext())
		{
			Player player = shieldIterator.next();

			if (!IsPowered(player))
			{
				shieldIterator.remove();
				SetPowered(player, false);
				continue;
			}
			
			if (UtilTime.elapsed(_active.get(player), 2000))
			{
				shieldIterator.remove();

				SetPowered(player, false);
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, 3f, 0.75f);
			}
		}
	}

	@EventHandler
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)		return;
		
		if (!Kit.HasKit(damagee))
			return;
		
		if (!IsPowered(damagee))
			return;
		
		event.SetCancelled("Lightning Shield");
		
		//Inform
		UtilPlayer.message(damagee, F.main("Skill", "You hit " + F.elem(UtilEnt.getName(event.GetDamagerPlayer(false))) + " with " + F.skill(GetName()) + "."));
		
		//Lightning
		damagee.getWorld().strikeLightningEffect(damagee.getLocation());
		SetPowered(damagee, false);
		
		//Damage Event
		Manager.GetDamage().NewDamageEvent(event.GetDamagerEntity(false), damagee, null, 
				DamageCause.LIGHTNING, 4, true, true, false,
				damagee.getName(), GetName());
	}
	
	public DisguiseCreeper GetDisguise(Player player)
	{
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise == null)
			return null;

		if (!(disguise instanceof DisguiseCreeper))
			return null;

		return (DisguiseCreeper)disguise;
	}
	
	public void SetPowered(Player player, boolean powered)
	{
		DisguiseCreeper creeper = GetDisguise(player);
		if (creeper == null)	return;
		
		creeper.SetPowered(powered);
		
		Manager.GetDisguise().updateDisguise(creeper);
	}
	
	public boolean IsPowered(Player player)
	{
		DisguiseCreeper creeper = GetDisguise(player);
		if (creeper == null)	return false;
		
		return creeper.IsPowered();
	}

	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2.5);
	}
}
