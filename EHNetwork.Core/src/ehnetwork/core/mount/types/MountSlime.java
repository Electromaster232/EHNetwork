package ehnetwork.core.mount.types;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.mount.Mount;
import ehnetwork.core.mount.MountManager;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MountSlime extends Mount<Slime>
{
	public MountSlime(MountManager manager)
	{
		super(manager, "Slime Mount", Material.SLIME_BALL, (byte)0, new String[]
				{
				ChatColor.RESET + "Bounce around on your very",
				ChatColor.RESET + "own personal slime friend!",
				}, 
				15000);
		
		KnownPackage = false;
	}

	public void EnableCustom(Player player)
	{
		player.leaveVehicle();
		player.eject();
		
		//Remove other mounts
		Manager.DeregisterAll(player);
		
		Slime mount = player.getWorld().spawn(player.getLocation(), Slime.class);
		mount.setSize(2);
		
		mount.setCustomName(player.getName() + "'s " + GetName());

		//Inform
		UtilPlayer.message(player, F.main("Mount", "You spawned " + F.elem(GetName()) + "."));
		
		//Store
		_active.put(player, mount);
	}
	
	public void Disable(Player player)
	{
		Slime mount = _active.remove(player);
		if (mount != null)
		{
			mount.remove();
			
			//Inform
			UtilPlayer.message(player, F.main("Mount", "You despawned " + F.elem(GetName()) + "."));
			
			Manager.removeActive(player);
		}	
	}
	
	@EventHandler
	public void interactMount(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() == null)
			return;
		
		if (!GetActive().containsKey(event.getPlayer()))
			return;
		
		if (!GetActive().get(event.getPlayer()).equals(event.getRightClicked()))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Mount", "This is not your Mount!"));
			return;
		}
		
		event.getPlayer().leaveVehicle();
		event.getPlayer().eject();
			
		event.getRightClicked().setPassenger(event.getPlayer());
	}
	
	@EventHandler
	public void target(EntityTargetEvent event)
	{	
		if (!GetActive().containsKey(event.getTarget()))
			return;
		
		if (!GetActive().get(event.getTarget()).equals(event.getEntity()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void updateBounce(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		//Bounce
		for (Slime slime : GetActive().values())
		{
			if (slime.getPassenger() == null)
				continue;
			
			if (!UtilEnt.isGrounded(slime))
				continue;
			
			if (!(slime.getPassenger() instanceof Player))
				continue;
			
			Player player = (Player)slime.getPassenger();
			
			if (!Recharge.Instance.use(player, GetName(), 200, false, false))
				continue;

			UtilAction.velocity(slime, slime.getPassenger().getLocation().getDirection(), 1, true, 0, 0.4, 1, true);
			
			slime.getWorld().playSound(slime.getLocation(), Sound.SLIME_WALK, 1f, 0.75f);
		}
		
		//Collide
		for (Slime slime : GetActive().values())
		{
			if (slime.getPassenger() == null)
				continue;
			
			if (!(slime.getPassenger() instanceof Player))
				continue;
			
			Player player = (Player)slime.getPassenger();
			
			if (!Recharge.Instance.usable(player, GetName() + " Collide"))
				continue;
			
			for (Slime other : GetActive().values())
			{
				if (other.equals(slime))
					continue;
				
				if (other.getPassenger() == null)
					continue;
				
				if (!(other.getPassenger() instanceof Player))
					continue;
				
				Player otherPlayer = (Player)other.getPassenger();
				
				if (!Recharge.Instance.usable(otherPlayer, GetName() + " Collide"))
					continue;
				
				//Collide
				if (UtilMath.offset(slime, other) > 2)
					continue;
				
				Recharge.Instance.useForce(player, GetName() + " Collide", 500);
				Recharge.Instance.useForce(otherPlayer, GetName() + " Collide", 500);
				
				UtilAction.velocity(slime, UtilAlg.getTrajectory(other, slime), 1.2, false, 0, 0.8, 10, true);
				UtilAction.velocity(other, UtilAlg.getTrajectory(slime, other), 1.2, false, 0, 0.8, 10, true);
				
				slime.getWorld().playSound(slime.getLocation(), Sound.SLIME_ATTACK, 1f, 0.5f);
				slime.getWorld().playSound(slime.getLocation(), Sound.SLIME_WALK, 1f, 0.5f);
				other.getWorld().playSound(other.getLocation(), Sound.SLIME_WALK, 1f, 0.5f);
				
				slime.playEffect(EntityEffect.HURT);
				other.playEffect(EntityEffect.HURT);
			}
		}
	}
	
	
}
