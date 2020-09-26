package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseBlaze;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphBlaze extends MorphGadget
{
	public MorphBlaze(GadgetManager manager)
	{
		super(manager, "Blaze Morph", new String[] 
				{
				C.cWhite + "Transforms the wearer into a fiery Blaze!",
				" ",
				C.cYellow + "Crouch" + C.cGray + " to use " + C.cGreen + "Firefly",
				" ",
				C.cPurple + "Unlocked with Hero Rank",
				},
				-1,
				Material.BLAZE_POWDER, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseBlaze disguise = new DisguiseBlaze(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}
	
	@EventHandler
	public void Trail(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
		{
			for (Player player : GetActive())
			{
				if (player.isSneaking())
				{
					player.leaveVehicle();
					player.eject();
					
					player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 0.2f, (float)(Math.random()));
					UtilParticle.PlayParticle(UtilParticle.ParticleType.FLAME, player.getLocation().add(0, 1, 0),
							0.25f, 0.25f, 0.25f, 0f, 3,
							UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
					UtilParticle.PlayParticle(UtilParticle.ParticleType.LARGE_SMOKE, player.getLocation().add(0, 1, 0),
							0.1f, 0.1f, 0.1f, 0f, 1,
							UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
					UtilAction.velocity(player, 0.8, 0.1, 1, true);
				}
			}
		}
	}
	
	@EventHandler
	public void HeroOwner(PlayerJoinEvent event)
	{
		if (Manager.getClientManager().Get(event.getPlayer()).GetRank().Has(Rank.HERO))
		{
			Manager.getDonationManager().Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(GetName());
		}	
	}
}
