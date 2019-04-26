package mineplex.core.mount.types;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.mount.HorseMount;
import mineplex.core.mount.MountManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class MountUndead extends HorseMount
{
	public MountUndead(MountManager manager) 
	{
		super(manager, "Infernal Horror", new String[]
				{
				C.cWhite + "The most ghastly horse in",
				C.cWhite + "existance, from the pits of",
				C.cWhite + "the Nether.",
				},
				Material.BONE,
				(byte)0,
				20000,
				Color.BLACK, Style.BLACK_DOTS, Variant.SKELETON_HORSE, 0.8, null);
	}

	@EventHandler
	public void Trail(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
			for (Horse horse : GetActive().values())
				UtilParticle.PlayParticle(ParticleType.FLAME, horse.getLocation().add(0, 1, 0),
						0.25f, 0.25f, 0.25f, 0, 2,
						ViewDist.NORMAL, UtilServer.getPlayers());

		if (event.getType() == UpdateType.FAST)
			for (Horse horse : GetActive().values())
				UtilParticle.PlayParticle(ParticleType.LAVA, horse.getLocation().add(0, 1, 0),
						0.25f, 0.25f, 0.25f, 0, 1,
						ViewDist.NORMAL, UtilServer.getPlayers());
		
	}
}
