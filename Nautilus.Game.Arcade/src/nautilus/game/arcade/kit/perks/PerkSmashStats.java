package nautilus.game.arcade.kit.perks;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PerkSmashStats extends Perk
{
	private double _damage;
	private double _knockbackTaken;
	private double _regen;
	
	public PerkSmashStats(double damage, double knockbackTaken, double regen, double armor) 
	{
		super("Smash Stats", new String[] 
				{
				
				(C.cAqua + "Damage: " + C.cWhite + damage) + C.cWhite + "        " + (C.cAqua + "Knockback Taken: " + C.cWhite + (int)(knockbackTaken*100) + "%"),
				(C.cAqua + "Armor: " + C.cWhite + armor) + C.cWhite + "          " + (C.cAqua + "Health Regeneration: " + C.cWhite + regen + " per Second"),
				});
		
		_damage = damage;
		_knockbackTaken = knockbackTaken;
		_regen = regen;
	} 
	
	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;
		
		double mod = _damage - event.GetDamageInitial();
				
		event.AddMod(damager.getName(), "Attack", mod, true);
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
		
		event.AddKnockback("Knockback Multiplier", _knockbackTaken);
	}
	
	@EventHandler
	public void Regeneration(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			UtilPlayer.health(player, _regen);
		}
	}
}
