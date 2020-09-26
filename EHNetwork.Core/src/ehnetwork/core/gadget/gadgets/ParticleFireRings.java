package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ParticleFireRings extends ParticleGadget
{

	public ParticleFireRings(GadgetManager manager)
	{
		super(manager, "Flame Rings", new String[] 
				{
				C.cWhite + "Forged from the burning ashes",
				C.cWhite + "of 1000 Blazes by the infamous",
				C.cWhite + "Flame King of the Nether realm.",
				},
				-2,
				Material.BLAZE_POWDER, (byte)0);
	}

	@EventHandler
	public void playParticle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (!shouldDisplay(player))
				continue;
			
			if (Manager.isMoving(player))
			{
				UtilParticle.PlayParticle(UtilParticle.ParticleType.FLAME, player.getLocation().add(0, 1f, 0), 0.2f, 0.2f, 0.2f, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				for (int i=0 ; i < 1 ; i++)
				{
					double lead = i * ((2d * Math.PI)/2);

					float x = (float) (Math.sin(player.getTicksLived()/5d + lead) * 1f);
					float z = (float) (Math.cos(player.getTicksLived()/5d + lead) * 1f);

					float y = (float) (Math.sin(player.getTicksLived()/5d + lead) + 1f);

					UtilParticle.PlayParticle(UtilParticle.ParticleType.FLAME, player.getLocation().add(x, y, z), 0f, 0f, 0f, 0, 1,
							UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
				}

				for (int i=0 ; i < 1 ; i++)
				{
					double lead = i * ((2d * Math.PI)/2);

					float x = (float) -(Math.sin(player.getTicksLived()/5d + lead) * 1f);
					float z = (float) (Math.cos(player.getTicksLived()/5d + lead) * 1f);

					float y = (float) (Math.sin(player.getTicksLived()/5d + lead) + 1f);

					UtilParticle.PlayParticle(UtilParticle.ParticleType.FLAME, player.getLocation().add(x, y, z), 0f, 0f, 0f, 0, 1,
							UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
				}	
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.FIRE, 0.2f, 1f);
			}
		}
	}
}
