package nautilus.game.arcade.managers;

import nautilus.game.arcade.ArcadeManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class GameSpectatorManager implements Listener
{
	ArcadeManager Manager;

	public GameSpectatorManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void interactCancel(PlayerInteractEvent event)
	{
		if (Manager.GetGame() == null)
			return;

		Player player = event.getPlayer();

		if (!Manager.GetGame().IsAlive(player))
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void interactEntityCancel(PlayerInteractEntityEvent event)
	{
		if (Manager.GetGame() == null)
			return;

		Player player = event.getPlayer();

		if (!Manager.GetGame().IsAlive(player))
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void vehicleDamage(VehicleDamageEvent event)
	{
		if (Manager.GetGame() == null)
			return;

		if (!(event.getAttacker() instanceof Player))
			return;
		
		Player player = (Player)event.getAttacker();

		if (!Manager.GetGame().IsAlive(player))
			event.setCancelled(true);
	}
}
