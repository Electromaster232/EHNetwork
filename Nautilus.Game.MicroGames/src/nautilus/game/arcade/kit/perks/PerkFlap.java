package nautilus.game.arcade.kit.perks;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilServer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkFlap extends SmashPerk
{
	private double _power;
	private boolean _control;
	
	public PerkFlap(double power, double heightLimit, boolean control) 
	{
		super("Flap", new String[] 
				{
				C.cYellow + "Tap Jump Twice" + C.cGray + " to " + C.cGreen + "Flap"
				});
		
		_power = power;
		_control = control;
	}

	@EventHandler
	public void FlightHop(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();
		
		if (Manager.isSpectator(player))
			return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		
		event.setCancelled(true);
		player.setFlying(false);
		
		//Disable Flight
		player.setAllowFlight(false);
		
		double power = 0.4 + 0.6 * (_power * player.getExp());
		
		//Velocity
		if (_control)
		{
			UtilAction.velocity(player, power, 0.2, 10, true);
		}
		else
		{
			UtilAction.velocity(player, player.getLocation().getDirection(), power, true, power, 0, 10, true);
		}
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, (float)(0.3 + player.getExp()), (float)(Math.random()/2+1));
		
		//Set Recharge
		Recharge.Instance.use(player, GetName(), 80, false, false);
		
		//Energy
		if (!isSuperActive(player))
			player.setExp(Math.max(0f, player.getExp() - (1f/6f)));
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
			
			if (!Kit.HasKit(player))
				continue;
			
			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) 
			{
				player.setExp(0.999f);
				player.setAllowFlight(true);
			}
			else if (Recharge.Instance.usable(player, GetName()) && player.getExp() > 0)
			{
				player.setAllowFlight(true);
			}
		}
	}
}
