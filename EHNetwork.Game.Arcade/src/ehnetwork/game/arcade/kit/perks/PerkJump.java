package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkJump extends Perk
{
	private int _level;
	
	public PerkJump(int level) 
	{
		super("Jump", new String[] 
				{ 
				C.cGray + "Permanent Jump Boost " + (level+1),
				});
		
		_level = level;
	}
		
	@EventHandler
	public void DigSpeed(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			Manager.GetCondition().Factory().Jump(GetName(), player, player, 8, _level, false, false, false);
		}
	}
}
