package mineplex.core.gadget.gadgets;

import org.bukkit.GameMode;
import org.bukkit.Material;
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

public class ParticleEnchant extends ParticleGadget
{
	public ParticleEnchant(GadgetManager manager)
	{
		super(manager, "Enchanted", new String[] 
				{
				C.cWhite + "The wisdom of the universe",
				C.cWhite + "suddenly finds you extremely",
				C.cWhite + "attractive, and wants to",
				C.cWhite + "\'enchant\' you.",
				},
				-2,
				Material.BOOK, (byte)0);
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
				UtilParticle.PlayParticle(ParticleType.ENCHANTMENT_TABLE, player.getLocation().add(0, 1, 0), 0.2f, 0.2f, 0.2f, 0, 4,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				UtilParticle.PlayParticle(ParticleType.ENCHANTMENT_TABLE, player.getLocation().add(0, 1.4, 0), 0f, 0f, 0f, 1, 4,
						ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
	}
}
