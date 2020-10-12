package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.data.IcePathData;

public class PerkIcePath extends Perk
{
	private HashSet<IcePathData> _data = new HashSet<IcePathData>();
	
	public PerkIcePath() 
	{
		super("Ice Path", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Ice Path"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 12000, true, true))
			return;
		
		player.setVelocity(new Vector(0,0,0));
		player.teleport(player.getLocation().add(0, 0.75, 0));
		
		_data.add(new IcePathData(player));
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Freeze(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<IcePathData> dataIterator = _data.iterator();
		
		while (dataIterator.hasNext())
		{
			IcePathData data =  dataIterator.next();
			
			Block block = data.GetNextBlock();
			
			if (block == null)
			{
				dataIterator.remove();
			}
			else
			{
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 79);
				Manager.GetBlockRestore().Add(block, 79, (byte)0, 6000);
			}
		}
	}
}
