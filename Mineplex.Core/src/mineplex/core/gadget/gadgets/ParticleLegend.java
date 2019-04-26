package mineplex.core.gadget.gadgets;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.gadget.types.ParticleGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;

public class ParticleLegend extends ParticleGadget
{
	public ParticleLegend(GadgetManager manager)
	{
		super(manager, "Legendary Aura", new String[] 
				{
				C.cWhite + "These mystic particle attach to",
				C.cWhite + "only the most legendary of players!",
				" ",
				C.cPurple + "Unlocked with Legend Rank",
				},
				-2,
				Material.ENDER_PORTAL, (byte)0);
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
			
			player.getWorld().playEffect(player.getLocation().add(0, 1, 0), Effect.ENDER_SIGNAL, 0);
		}
	}
	
	@EventHandler
	public void legendOwner(PlayerJoinEvent event)
	{
		if (Manager.getClientManager().Get(event.getPlayer()).GetRank().Has(Rank.LEGEND))
		{
			Manager.getDonationManager().Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(GetName());
		}	
	}
}
