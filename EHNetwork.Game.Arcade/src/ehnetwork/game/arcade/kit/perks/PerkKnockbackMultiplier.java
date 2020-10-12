package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkKnockbackMultiplier extends Perk
{
	private double _power;
	
	public PerkKnockbackMultiplier(double power) 
	{
		super("Knockback", new String[] 
				{
				C.cGray + "You take " + (int)(power*100) + "% Knockback.",
				});
		
		_power = power;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;
		
		if (!Kit.HasKit(damagee))
			return;
		
		if (!Manager.IsAlive(damagee))
			return;
		
		event.AddKnockback("Knockback Multiplier", _power);
	}
}
