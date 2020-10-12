package ehnetwork.hub.modules;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.hub.HubManager;

public class JumpManager extends MiniPlugin
{
	public HubManager Manager;
	
	public JumpManager(HubManager manager)
	{
		super("Double Jump", manager.getPlugin());
		
		Manager = manager;
	}

	@EventHandler
	public void FlightHop(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		
		//Chicken Cancel
		//DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		//if (disguise != null &&
		//		((disguise instanceof DisguiseChicken && !((DisguiseChicken)disguise).isBaby()) || disguise instanceof DisguiseBat || disguise instanceof DisguiseEnderman || disguise instanceof DisguiseWither))
		//	return;
		
		event.setCancelled(true);
		player.setFlying(false);

		//Disable Flight
		player.setAllowFlight(false);
		
		//Velocity
		UtilAction.velocity(player, 1.4, 0.2, 1, true);

		//Sound
		player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
	}

	@EventHandler
	public void FlightUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE)
				continue;
		
			if (Manager.GetParkour().isParkourMode(player))
			{
				player.setAllowFlight(false);
				player.setFlying(false);
				continue;
			}
			
			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN)))
			{
				player.setAllowFlight(true);
				player.setFlying(false);
			}
		}
	}
}
