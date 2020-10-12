package ehnetwork.game.survival.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseBat;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.core.disguise.disguises.DisguiseEnderman;
import ehnetwork.core.disguise.disguises.DisguiseWither;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.survival.SurvivalManager;

public class JumpManager extends MiniPlugin
{
	public SurvivalManager Manager;
	private List<Player> _players = new ArrayList<Player>();
	public JumpManager(SurvivalManager manager)
	{
		super("Double Jump", manager.getPlugin());

		Manager = manager;
	}

	@EventHandler
	public void FlightHop(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();

		if(!_players.contains(player)){
			return;
		}

		if(player.getAllowFlight()){
			return;
		}

		//Chicken Cancel
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null &&
				((disguise instanceof DisguiseChicken && !((DisguiseChicken)disguise).isBaby()) || disguise instanceof DisguiseBat || disguise instanceof DisguiseEnderman || disguise instanceof DisguiseWither))
			return;

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

	}

	public void addPlayer(Player caller){
		_players.add(caller);
	}

	public void delPlayer(Player caller){
		_players.remove(caller);
	}

	public boolean checkPlayer(Player caller){
		return _players.contains(caller);
	}
}
