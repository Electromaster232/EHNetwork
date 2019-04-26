package mineplex.core.gadget.gadgets;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.gadget.types.ParticleGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;

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
				UtilParticle.PlayParticle(ParticleType.SPLASH, player.getLocation().add(0, 1, 0), 0.2f, 0.2f, 0.2f, 0, 4,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				UtilParticle.PlayParticle(ParticleType.EXPLODE, player.getLocation().add(0, 3.5, 0), 0.6f, 0f, 0.6f, 0, 8,
						ViewDist.NORMAL, player);
				
				for (Player other : UtilServer.getPlayers())
					if (!player.equals(other))
						UtilParticle.PlayParticle(ParticleType.CLOUD, player.getLocation().add(0, 3.5, 0), 0.6f, 0.1f, 0.6f, 0, 8,
								ViewDist.NORMAL, other);
				
				UtilParticle.PlayParticle(ParticleType.DRIP_WATER, player.getLocation().add(0, 3.5, 0), 0.4f, 0.1f, 0.4f, 0, 2,
						ViewDist.NORMAL, UtilServer.getPlayers());
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_RAIN, 0.1f, 1f);
			}
		}
	}
}
