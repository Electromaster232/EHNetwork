package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkSlow extends Perk
{
	private int _level;
	
	public PerkSlow(int level) 
	{
		super("Slow", new String[] 
				{ 
				C.cGray + "Permanent Slow " + (level+1),
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
			
			Manager.GetCondition().Factory().Slow(GetName(), player, player, 8, _level, false, false, false, false);
		}
	}
}
