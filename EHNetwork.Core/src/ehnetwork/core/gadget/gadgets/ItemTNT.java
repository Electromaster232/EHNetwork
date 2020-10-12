package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ItemTNT extends ItemGadget
{
	private HashSet<TNTPrimed> _tnt = new HashSet<TNTPrimed>();
	
	public ItemTNT(GadgetManager manager)
	{
		super(manager, "TNT", new String[] 
				{
					C.cWhite + "Blow some people up!",
					C.cWhite + "KABOOM!",
				}, 
				-1,  
				Material.TNT, (byte)0, 
				1000, new Ammo("TNT", "20 TNT", Material.TNT, (byte)0, new String[] { C.cWhite + "20 TNT for you to explode!" }, 500, 20));
	}

	@Override
	public void ActivateCustom(Player player)
	{
		TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);
		UtilAction.velocity(tnt, player.getLocation().getDirection(), 0.6, false, 0, 0.2, 1, false);
		_tnt.add(tnt);

		//Inform
		UtilPlayer.message(player, F.main("Skill", "You threw " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Update(EntityExplodeEvent event)
	{
		if (!(event.getEntity() instanceof TNTPrimed))
			return;
		
		if (!_tnt.remove(event.getEntity()))
			return;
		
		HashMap<Player, Double> players = UtilPlayer.getInRadius(event.getLocation(), 10);
		for (Player player : players.keySet())
		{	
			if (Manager.collideEvent(this, player))
				continue;
			
			double mult = players.get(player);
					
			//Knockback
			UtilAction.velocity(player, UtilAlg.getTrajectory(event.getLocation(), player.getLocation()), 3 * mult, false, 0, 0.5 + 2 * mult, 10, true);
		}
	}

	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<TNTPrimed> tntIterator = _tnt.iterator();
		
		while (tntIterator.hasNext())
		{
			TNTPrimed tnt = tntIterator.next();
			
			if (!tnt.isValid() || tnt.getTicksLived() > 200)
			{
				tnt.remove();
				tntIterator.remove();
			}
		}
	}
}
