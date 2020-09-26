package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkDigger extends Perk
{
	public PerkDigger() 
	{
		super("Digger", new String[] 
				{ 
				C.cGray + "Permanent Fast Digging II",
				});
	}
		
	@EventHandler
	public void DigSpeed(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			Manager.GetCondition().Factory().DigFast(GetName(), player, player, 2.9, 1, false, false, true);
		}
	}
}
