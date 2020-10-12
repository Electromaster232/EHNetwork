package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkDamageSnow extends Perk
{
	private int _damage;
	private double _knockback;
	
	public PerkDamageSnow(int damage, double knockback) 
	{
		super("Snow Attack", new String[] 
				{
				C.cGray + "+" + damage + " Damage and " + (int)((knockback-1)*100) + "% Knockback to enemies on snow.",
				});
		
		_damage = damage;
		_knockback = knockback;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity().getLocation().getBlock().getTypeId() != 78)
			return;
		
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;
				
		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;
		
		event.AddMod(damager.getName(), GetName(), _damage, false);
		event.AddKnockback("Knockback Snow", _knockback);
	}
}
