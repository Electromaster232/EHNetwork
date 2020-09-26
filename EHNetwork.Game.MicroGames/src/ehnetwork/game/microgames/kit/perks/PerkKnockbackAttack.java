package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkKnockbackAttack extends Perk
{
	private double _power;
	
	public PerkKnockbackAttack(double power) 
	{
		super("Melee Knockback", new String[] 
				{
				C.cGray + "Melee attacks deal " + (int)(power*100) + "% Knockback.",
				});
		
		_power = power;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;

		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;

		event.AddKnockback("Knockback Melee", _power);
	}
}
