package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkCleave extends Perk
{
	private boolean _axeOnly;
	
	public PerkCleave(double splash, boolean axeOnly) 
	{
		super("Cleave", new String[] 
				{ 
				C.cGray + "Attacks deal " + (int)(100*splash) + "% damage to nearby enemies",
				});
		
		_axeOnly = axeOnly;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Skill(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		if (event.GetReason() != null)
			return;
		
		//Dont allow usage in early game
		if (UtilTime.elapsed(Manager.GetGame().GetStateTime(), 30000))
			return;

		//Damager
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (_axeOnly && !UtilGear.isAxe(damager.getItemInHand()))
			return;
		
		if (!UtilGear.isWeapon(damager.getItemInHand()))
			return;

		if (!Kit.HasKit(damager))
			return;

		//Damagee
		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		//Damage
		event.AddMod(damager.getName(), GetName(), 0, false);

		//Splash
		for (Player other : UtilPlayer.getNearby(damagee.getLocation(), 3))
		{
			if (other.equals(damagee))
				continue;
			
			if (other.equals(damager))
				continue;

			if (!Manager.canHurt(damager, other))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(other, damager, null, 
					DamageCause.CUSTOM, event.GetDamageInitial(), true, true, false,
					damager.getName(), GetName());
		}
	}
}
