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

public class ParticleRain extends ParticleGadget
{

	public ParticleRain(GadgetManager manager)
	{
		super(manager, "Rain Cloud", new String[] 
				{
				C.cWhite + "Your very own rain cloud!",
				C.cWhite + "Now you never have to worry",
				C.cWhite + "about not being wet. Woo...",
				},
				-2,
				Material.INK_SACK, (byte)4);
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
				UtilParticle.PlayParticle(UtilParticle.ParticleType.SPLASH, player.getLocation().add(0, 1, 0), 0.2f, 0.2f, 0.2f, 0, 4,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				UtilParticle.PlayParticle(UtilParticle.ParticleType.EXPLODE, player.getLocation().add(0, 3.5, 0), 0.6f, 0f, 0.6f, 0, 8,
						UtilParticle.ViewDist.NORMAL, player);
				
				for (Player other : UtilServer.getPlayers())
					if (!player.equals(other))
						UtilParticle.PlayParticle(UtilParticle.ParticleType.CLOUD, player.getLocation().add(0, 3.5, 0), 0.6f, 0.1f, 0.6f, 0, 8,
								UtilParticle.ViewDist.NORMAL, other);
				
				UtilParticle.PlayParticle(UtilParticle.ParticleType.DRIP_WATER, player.getLocation().add(0, 3.5, 0), 0.4f, 0.1f, 0.4f, 0, 2,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_RAIN, 0.1f, 1f);
			}
		}
	}
}
