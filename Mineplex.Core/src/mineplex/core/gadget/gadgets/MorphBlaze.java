package mineplex.core.gadget.gadgets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.disguise.disguises.DisguiseBlaze;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

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
					UtilParticle.PlayParticle(ParticleType.FLAME, player.getLocation().add(0, 1, 0), 
							0.25f, 0.25f, 0.25f, 0f, 3,
							ViewDist.NORMAL, UtilServer.getPlayers());
					UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 
							0.1f, 0.1f, 0.1f, 0f, 1,
							ViewDist.NORMAL, UtilServer.getPlayers());
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
