package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkArcticAura extends Perk
{
	public PerkArcticAura() 
	{
		super("Arctic Aura", new String[] 
				{ 
				"You freeze things around you, slowing enemies."
				});
	}
	
	@EventHandler 
	public void SnowAura(UpdateEvent event) 
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{	
			if (!Kit.HasKit(player))
				continue;
			
			if (((CraftPlayer) player).getHandle().spectating)
				continue;
			
			double range = 5*player.getExp();
			
			//Blocks
			double duration = 2000;
			HashMap<Block, Double> blocks = UtilBlock.getInRadius(player.getLocation(), range);
			for (Block block : blocks.keySet())
			{			
				//Snow
				Manager.GetBlockRestore().Snow(block, (byte)1, (byte)1, (long)(duration * (1 + blocks.get(block))), 250, 0);
			}
			
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (other.equals(player))
					continue;
				
				if (UtilMath.offset(player, other) > range)
					continue;
				
				Manager.GetCondition().Factory().Slow("Aura Slow", other, player, 0.9, 0, false, false, false, false);
			}
		}
	}
}
