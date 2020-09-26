package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkStrength extends Perk
{
	private int _power;
	
	public PerkStrength(int power) 
	{
		super("Strength", new String[] 
				{ 
				C.cGray + "You deal " + power + " more damage",
				});
		
		_power = power;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void DamageDecrease(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;
		
		if (!Kit.HasKit(damager))
			return;
		
		event.AddMod(damager.getName(), GetName(), _power, false);
	}
}
