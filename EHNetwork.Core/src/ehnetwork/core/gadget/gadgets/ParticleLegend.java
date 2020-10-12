package ehnetwork.core.gadget.gadgets;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

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
