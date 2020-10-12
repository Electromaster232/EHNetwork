package ehnetwork.hub.modules;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.hub.HubManager;

public class ForcefieldManager extends MiniPlugin
{
	public HubManager Manager;

	private HashMap<Player, Integer> _radius = new HashMap<Player, Integer>();

	public ForcefieldManager(HubManager manager)
	{
		super("Forcefield", manager.getPlugin());

		Manager = manager;
	}

	@EventHandler
	public void ForcefieldUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (Manager.getPreferences().Get(player).HubForcefield && (Manager.GetClients().Get(player).GetRank().Has(Rank.ADMIN) || Manager.GetClients().Get(player).GetRank() == Rank.JNR_DEV))
			{
				for (Player other : UtilServer.getPlayers())
				{
					if (player.equals(other))
						continue;

					int range = 5;
					if (_radius.containsKey(player))
						range = _radius.get(player);

					if (UtilMath.offset(other, player) > range)
						continue;

					if (Manager.GetClients().Get(other).GetRank().Has(Rank.ADMIN) || Manager.GetClients().Get(other).GetRank() == Rank.JNR_DEV)
						continue;

					if (Recharge.Instance.use(other, "Forcefield Bump", 500, false, false))
					{
						Entity bottom = other;
						while (bottom.getVehicle() != null)
							bottom = bottom.getVehicle();
						
						UtilAction.velocity(bottom, UtilAlg.getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10, true);
						other.getWorld().playSound(other.getLocation(), Sound.CHICKEN_EGG_POP, 2f, 0.5f);
					}
				}
			}
		}
	}

	public void ForcefieldRadius(Player caller, String[] args) 
	{
		try
		{
			int range = Integer.parseInt(args[0]);

			_radius.put(caller, range);

			UtilPlayer.message(caller, F.main("Forcefield", "Radius set to " + F.elem(range + "") + "."));
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("Forcefield", "Invalid Input. Correct input is " + F.elem("/radius #") + "."));
		}
	}

	@EventHandler
	public void ForcefieldReset(PlayerQuitEvent event)
	{
		_radius.remove(event.getPlayer());
	}
}
