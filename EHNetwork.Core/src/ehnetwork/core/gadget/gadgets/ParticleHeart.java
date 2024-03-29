package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ParticleHeart extends ParticleGadget
{
	private HashMap<Player, HashMap<Player, Location>> _target = new HashMap<Player, HashMap<Player, Location>>();

	public ParticleHeart(GadgetManager manager)
	{
		super(manager, "I Heart You", new String[] 
				{
				C.cWhite + "With these particles, you can",
				C.cWhite + "show off how much you heart",
				C.cWhite + "everyone on Mineplex!",
				},
				-2,
				Material.APPLE, (byte)0);
	}

	@EventHandler
	public void playParticle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		//Launch
		for (Player player : GetActive())
		{
			if (!shouldDisplay(player))
				continue;


			if (!_target.containsKey(player))
				_target.put(player, new HashMap<Player, Location>());

			if (Recharge.Instance.use(player, GetName(), 500, false, false))
			{
				for (Player other : UtilServer.getPlayers())
				{
					if (other.equals(player))
						continue;

					if (!UtilPlayer.isSpectator(other))
						continue;

					if (_target.get(player).containsKey(other))
						continue;

					if (UtilMath.offset(player, other) > 6)
						continue;

					_target.get(player).put(other, player.getLocation().add(0, 1, 0));

					break;
				}
			}

			if (Manager.isMoving(player))
				UtilParticle.PlayParticle(UtilParticle.ParticleType.HEART, player.getLocation().add(0, 1, 0), 0f, 0f, 0f, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			else
				UtilParticle.PlayParticle(UtilParticle.ParticleType.HEART, player.getLocation().add(0, 1, 0), 0.5f, 0.5f, 0.5f, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
		}


		//Particle
		for (HashMap<Player, Location> heart : _target.values())
		{
			Iterator<Entry<Player, Location>> heartIterator = heart.entrySet().iterator();

			while (heartIterator.hasNext())
			{
				Entry<Player, Location> entry = heartIterator.next();

				entry.getValue().add(UtilAlg.getTrajectory(entry.getValue(), entry.getKey().getEyeLocation()).multiply(0.6));

				UtilParticle.PlayParticle(UtilParticle.ParticleType.HEART, entry.getValue(), 0, 0, 0, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());

				if (UtilMath.offset(entry.getValue(), entry.getKey().getEyeLocation()) < 0.6)
					heartIterator.remove();
			}
		}
	}

	@Override
	public void DisableCustom(Player player)
	{
		if (_active.remove(player))
			UtilPlayer.message(player, F.main("Gadget", "You unsummoned " + F.elem(GetName()) + "."));

		clean(player);
	}

	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		clean(event.getPlayer());
	}

	private void clean(Player player)
	{
		_target.remove(player);

		for (HashMap<Player, Location> map : _target.values())
			map.remove(player);
	}
}
