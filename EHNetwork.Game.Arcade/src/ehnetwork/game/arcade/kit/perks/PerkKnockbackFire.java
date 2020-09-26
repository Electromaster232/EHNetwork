package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkKnockbackFire extends Perk
{
	private double _power;
	
	public PerkKnockbackFire(double power) 
	{
		super("Flaming Knockback", new String[] 
				{
				C.cGray + "You deal " + (int)(power*100) + "% Knockback to burning enemies.",
				});
		
		_power = power;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity().getFireTicks() <= 0)
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;
				
		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;
		
		event.AddKnockback("Knockback Fire", _power);
	}
}
