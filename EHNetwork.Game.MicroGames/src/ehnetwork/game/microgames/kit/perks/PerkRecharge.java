package ehnetwork.game.microgames.kit.perks;

import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.recharge.RechargeEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkRecharge extends Perk
{
	private double _reduction;

	public PerkRecharge(double reduction) 
	{
		super("Recharge", new String[] 
				{ 
				C.cGray + "Reduces ability cooldowns by " + (int)(reduction*100) + "%",
				});

		_reduction = reduction;
	}

	@EventHandler
	public void DigSpeed(RechargeEvent event)
	{
		if (!Kit.HasKit(event.GetPlayer()))
			return;
		
		event.SetRecharge((long) (event.GetRecharge() * (1 - _reduction)));
	}
}
