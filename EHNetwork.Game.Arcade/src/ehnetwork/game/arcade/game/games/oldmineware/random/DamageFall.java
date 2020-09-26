package ehnetwork.game.arcade.game.games.oldmineware.random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.game.arcade.game.games.oldmineware.OldMineWare;
import ehnetwork.game.arcade.game.games.oldmineware.order.Order;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DamageFall extends Order 
{
	public DamageFall(OldMineWare host) 
	{
		super(host, "Take fall damage");
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
		if (event.GetCause() != DamageCause.FALL)
			return;
		
		Player player = event.GetDamageePlayer();
		if (player == null)	return;
		
		SetCompleted(player);
	}
}
