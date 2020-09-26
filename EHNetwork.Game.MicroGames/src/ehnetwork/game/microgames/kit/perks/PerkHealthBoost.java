package ehnetwork.game.microgames.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkHealthBoost extends Perk
{
	private int _level;

	public PerkHealthBoost(int level)
	{
		super("Speed", new String[] 
				{ 
				C.cGray + "Permanent Health Boost " + (level+1),
				});
		
		_level = level;
	}
		
	@EventHandler
	public void Boost(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			Manager.GetCondition().Factory().HealthBoost(GetName(), player, player, 8, _level, false, false, true);
		}
	}
}
