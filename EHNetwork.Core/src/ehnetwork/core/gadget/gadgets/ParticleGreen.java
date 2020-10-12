package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ParticleGreen extends ParticleGadget
{

	public ParticleGreen(GadgetManager manager)
	{
		super(manager, "Green Ring", new String[] 
				{
				C.cWhite + "With these sparkles, you",
				C.cWhite + "can now sparkle while you",
				C.cWhite + "sparkle with CaptainSparklez.",
				},
				-2,
				Material.EMERALD, (byte)0);
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
			
			float x = (float) (Math.sin(player.getTicksLived()/7d) * 1f);
			float z = (float) (Math.cos(player.getTicksLived()/7d) * 1f);
			float y = (float) (Math.cos(player.getTicksLived()/17d) * 1f + 1f);
			
			UtilParticle.PlayParticle(UtilParticle.ParticleType.HAPPY_VILLAGER, player.getLocation().add(x, y, z), 0f, 0f, 0f, 0, 1,
					UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			
//			if (Manager.isMoving(player))
//			{
//				UtilParticle.PlayParticle(ParticleType.HAPPY_VILLAGER, player.getLocation().add(0, 1f, 0), 0f, 0f, 0f, 0, 1);
//			}
//			else
//			{
//				float scale = Math.abs((float) (Math.sin(player.getTicksLived()/30d) * 2f)) + 1;
//			//	float vertical = (float) (Math.cos(player.getTicksLived()/50d) * 1f);
//				
//				int dir = player.isSneaking() ? 1 : -1;
//				
//				for (double i=0 ; i<Math.PI * 2 ; i += 0.2)
//				{
//					double x = Math.sin(i + (dir * player.getTicksLived()/50d)) * (i%(Math.PI/2d));
//					double z = Math.cos(i + (dir * player.getTicksLived()/50d)) * (i%(Math.PI/2d));
//					
//					UtilParticle.PlayParticle(ParticleType.HAPPY_VILLAGER, player.getLocation().add(x, 1, z), 0f, 0f, 0f, 0, 1);
//				}
//			}
		}
	}
}
