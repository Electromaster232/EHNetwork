package mineplex.core.gadget.gadgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.gadget.types.ParticleGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;

public class ParticleFoot extends ParticleGadget
{
	private boolean _foot = false;
	
	private HashMap<Location, Long> _steps = new HashMap<Location, Long>();
	
	public ParticleFoot(GadgetManager manager)
	{
		super(manager, "Shadow Walk", new String[] 
				{
				C.cWhite + "In a world where footprints",
				C.cWhite + "do not exist, leaving your",
				C.cWhite + "shadow behind is the next",
				C.cWhite + "best thing.",
				},
				-2,
				Material.LEATHER_BOOTS, (byte)0);
	}

	@EventHandler
	public void playParticle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;
		
		_foot = !_foot;

		cleanSteps();
		
		for (Player player : GetActive())
		{
			if (!shouldDisplay(player))
				continue;
			
			if (!Manager.isMoving(player))
				continue;
			
			if (!UtilEnt.isGrounded(player))
				continue;
			
			Vector offset;
			
			Vector dir = player.getLocation().getDirection();
			dir.setY(0);
			dir.normalize();
			
			if (_foot)
			{
				offset = new Vector(dir.getZ() * -1, 0.1, dir.getX());
			}
			else
			{
				offset = new Vector(dir.getZ(), 0.1, dir.getX() * -1);
			}
			
			Location loc = player.getLocation().add(offset.multiply(0.2));
			
			if (nearStep(loc))
				continue;
			
			if (!UtilBlock.solid(loc.getBlock().getRelative(BlockFace.DOWN)))
				continue;
			
			_steps.put(loc, System.currentTimeMillis());

			UtilParticle.PlayParticle(ParticleType.FOOTSTEP, loc, 0f, 0f, 0f, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
			
			UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, loc.clone().add(0, 0.1, 0), 0f, 0f, 0f, 0, 1,
					ViewDist.NORMAL, UtilServer.getPlayers());
		}
	}
	
	public void cleanSteps()
	{
		if (_steps.isEmpty())
			return;
		
		Iterator<Entry<Location, Long>> stepIterator = _steps.entrySet().iterator();
		
		while (stepIterator.hasNext())
		{
			Entry<Location, Long> entry = stepIterator.next();

			if (UtilTime.elapsed(entry.getValue(), 10000))
				stepIterator.remove();
		}
	}
	
	public boolean nearStep(Location loc)
	{
		for (Location other : _steps.keySet())
		{
			if (UtilMath.offset(loc, other) < 0.3)
				return true;
		}
		
		return false;
	}
}
