package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkIronSkin extends Perk
{
	private double _reduction;
	
	public PerkIronSkin(double d) 
	{
		super("Iron Skin", new String[] 
				{ 
				C.cGray + "You take " + d + " less damage from attacks",
				});
		
		_reduction = d;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void DamageDecrease(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() == DamageCause.FIRE_TICK)
			return;
		
		if (event.GetDamage() <= 1)
			return;
		
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)	return;
		
		if (!Kit.HasKit(damagee))
			return;
		
		event.AddMod(damagee.getName(), GetName(), -_reduction, false);
	}
}
