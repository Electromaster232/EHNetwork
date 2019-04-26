package mineplex.core.mount.types;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.mount.Mount;
import mineplex.core.mount.MountManager;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class MountCart extends Mount<Minecart>
{
	public MountCart(MountManager manager)
	{
		super(manager, "Minecart", Material.MINECART, (byte)0, new String[]
				{
				ChatColor.RESET + "Cruise around town in your",
				ChatColor.RESET + "new Minecart VX Turbo!",
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
		
		Minecart mount = player.getWorld().spawn(player.getLocation().add(0, 2, 0), Minecart.class);
		
		//Inform
		UtilPlayer.message(player, F.main("Mount", "You spawned " + F.elem(GetName()) + "."));
		
		//Store
		_active.put(player, mount);
	}
	
	public void Disable(Player player)
	{
		Minecart mount = _active.remove(player);
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
		for (Minecart cart : GetActive().values())
		{
			if (cart.getPassenger() == null)
				continue;
			
			if (!UtilEnt.isGrounded(cart))
				continue;
			
			if (!(cart.getPassenger() instanceof Player))
				continue;
			
			UtilAction.velocity(cart, cart.getPassenger().getLocation().getDirection(), 1.4, true, 0, 0, 1, false);
			
			if (Math.random() > 0.8)
				cart.getWorld().playSound(cart.getLocation(), Sound.MINECART_BASE, 0.05f, 2f);
		}
		
		//Collide
		for (Minecart cart : GetActive().values())
		{
			if (cart.getPassenger() == null)
				continue;
			
			if (!(cart.getPassenger() instanceof Player))
				continue;
			
			Player player = (Player)cart.getPassenger();
			
			if (!Recharge.Instance.usable(player, GetName() + " Collide"))
				continue;
			
			for (Minecart other : GetActive().values())
			{
				if (other.equals(cart))
					continue;
				
				if (other.getPassenger() == null)
					continue;
				
				if (!(other.getPassenger() instanceof Player))
					continue;
				
				Player otherPlayer = (Player)other.getPassenger();
				
				if (!Recharge.Instance.usable(otherPlayer, GetName() + " Collide"))
					continue;
				
				//Collide
				if (UtilMath.offset(cart, other) > 2)
					continue;
				
				Recharge.Instance.useForce(player, GetName() + " Collide", 500);
				Recharge.Instance.useForce(otherPlayer, GetName() + " Collide", 500);
				
				UtilAction.velocity(cart, UtilAlg.getTrajectory(other, cart), 1.2, false, 0, 0.8, 10, true);
				UtilAction.velocity(other, UtilAlg.getTrajectory(cart, other), 1.2, false, 0, 0.8, 10, true);
				
				cart.getWorld().playSound(cart.getLocation(), Sound.IRONGOLEM_HIT, 1f, 0.5f);
				other.getWorld().playSound(other.getLocation(), Sound.IRONGOLEM_HIT, 1f, 0.5f);
				
				//player.playEffect(EntityEffect.HURT);
				//otherPlayer.playEffect(EntityEffect.HURT);
			}
		}
	}
	
	@EventHandler
	public void cancelBreak(VehicleDamageEvent event)
	{
		if (GetActive().values().contains(event.getVehicle()))
			event.setCancelled(true);
	}
}
