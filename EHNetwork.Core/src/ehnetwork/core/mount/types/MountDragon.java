package ehnetwork.core.mount.types;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.mount.DragonData;
import ehnetwork.core.mount.DragonMount;
import ehnetwork.core.mount.MountManager;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MountDragon extends DragonMount
{
	public MountDragon(MountManager manager)
	{
		super(manager, "Ethereal Dragon", new String[]
				{
				C.cWhite + "From the distant ether realm,",
				C.cWhite + "this prized dragon is said to",
				C.cWhite + "obey only true Heroes!",
				" ",
				C.cPurple + "Unlocked with Hero Rank",
				},
				Material.DRAGON_EGG,
				(byte)0,
				-1);
	} 
	
	@EventHandler
	public void Trail(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
		{
			for (DragonData data : GetActive().values())
			{
				UtilParticle.PlayParticle(UtilParticle.ParticleType.WITCH_MAGIC, data.Dragon.getLocation().add(0, 1, 0),
						1f, 1f, 1f, 0f, 20,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
	}

	@EventHandler
	public void DragonLocation(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (DragonData data : GetActive().values())
			data.Move();
		
		HashSet<Player> toRemove = new HashSet<Player>();
		
		for (Player player : GetActive().keySet())
		{
			DragonData data = GetActive().get(player);
			if (data == null)
			{
				toRemove.add(player);
				continue;
			}
			
			if (!data.Dragon.isValid() || data.Dragon.getPassenger() == null || data.Dragon.getPassenger().getPassenger() == null)
			{
				data.Dragon.remove();
				toRemove.add(player);
				continue;
			}
		}
		
		for (Player player : toRemove)
			Disable(player);
	}
	
	@EventHandler
	public void DragonTargetCancel(EntityTargetEvent event)
	{
		if (GetActive().containsValue(event.getEntity()))
			event.setCancelled(true);	
	}

	public void SetName(String news) 
	{
		for (DragonData dragon : GetActive().values())
			dragon.Dragon.setCustomName(news);
	}

	public void setHealthPercent(double healthPercent)
	{
		for (DragonData dragon : GetActive().values())
		{
			double health = healthPercent * dragon.Dragon.getMaxHealth();
			if (health <= 0.0)
				health = 0.001;
			dragon.Dragon.setHealth(health);
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
