package ehnetwork.game.microgames.kit.perks;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkKnockback extends Perk
{
	private double _power;
	
	public PerkKnockback(double power) 
	{
		super("Knockback", new String[] 
				{
				C.cGray + "Attacks gives knockback with " + power + " power.",
				});
		
		_power = power;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;
		
		if (!Kit.HasKit(damager))
			return;
		
		if (!Manager.IsAlive(damager))
			return;
				
		event.SetKnockback(false);
		
		if (!Recharge.Instance.use(damager, "KB " + UtilEnt.getName(event.GetDamageeEntity()), 400, false, false))
			return;
		
		event.GetDamageeEntity().playEffect(EntityEffect.HURT);
		
		UtilAction.velocity(event.GetDamageeEntity(), 
				UtilAlg.getTrajectory(damager, event.GetDamageeEntity()), 
				_power, false, 0, 0.1, 10, true);
	}
}
