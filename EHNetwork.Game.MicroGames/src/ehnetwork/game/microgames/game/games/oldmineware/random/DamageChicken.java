package ehnetwork.game.microgames.game.games.oldmineware.random;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.game.microgames.game.games.oldmineware.OldMineWare;
import ehnetwork.game.microgames.game.games.oldmineware.order.Order;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DamageChicken extends Order 
{
	public DamageChicken(OldMineWare host) 
	{
		super(host, "punch a chicken");
	}

	@Override
	public void Initialize() 
	{
		
	}

	@Override
	public void Uninitialize()
	{
		
	}

	@Override
	public void FailItems(Player player) 
	{
		
	}
	
	@EventHandler
	public void Damage(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		Player player = event.GetDamagerPlayer(false);
		if (player == null)	return;
		
		LivingEntity ent = event.GetDamageeEntity();
		if (ent == null)	return;
		
		if (!(ent instanceof Chicken))
			return;
		
		SetCompleted(player);
	}
}
