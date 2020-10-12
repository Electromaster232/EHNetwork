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

public class ParticleHelix extends ParticleGadget
{

	public ParticleHelix(GadgetManager manager)
	{
		super(manager, "Blood Helix", new String[] 
				{
				C.cWhite + "Ancient legend says this magic",
				C.cWhite + "empowers the blood of its user,",
				C.cWhite + "giving them godly powers.",
				},
				-2,
				Material.REDSTONE, (byte)0);
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
				UtilParticle.PlayParticle(UtilParticle.ParticleType.RED_DUST, player.getLocation().add(0, 1, 0), 0.2f, 0.2f, 0.2f, 0, 4,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				for (int height=0 ; height <= 20 ; height++)
				{
					for (int i=0 ; i < 2 ; i++)
					{
						double lead = i * ((2d * Math.PI)/2);

						double heightLead = height * ((2d * Math.PI)/20);
						
						float x = (float) (Math.sin(player.getTicksLived()/20d + lead + heightLead) * 1.2f);
						float z = (float) (Math.cos(player.getTicksLived()/20d + lead + heightLead) * 1.2f);

						float y = 0.15f * height;

						UtilParticle.PlayParticle(UtilParticle.ParticleType.RED_DUST, player.getLocation().add(x * (1d - height/22d), y, z * (1d - height/22d)), 0f, 0f, 0f, 0, 1,
								UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
						
						
					}
				}
				
				//UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0, 3, 0), 0f, 0f, 0f, 0, 2);
	
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.LAVA, 0.3f, 1f);
			}
		}
	}
}
