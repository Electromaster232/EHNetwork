package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class ParticleFairy extends ParticleGadget
{
	private HashMap<Player, ParticleFairyData> _fairy = new HashMap<Player, ParticleFairyData>();

	public ParticleFairy(GadgetManager manager)
	{
		super(manager, "Flame Fairy", new String[] 
				{
				C.cWhite + "HEY! LISTEN!",
				C.cWhite + "HEY! LISTEN!",
				C.cWhite + "HEY! LISTEN!",
				},
				-2,
				Material.BLAZE_POWDER, (byte)0);
	}

	@EventHandler
	public void playParticle(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		//Launch
		for (Player player : GetActive())
		{
			if (!shouldDisplay(player))
				continue;

			//Create
			if (!_fairy.containsKey(player))
				_fairy.put(player, new ParticleFairyData(player));

			_fairy.get(player).Update();
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
		_fairy.remove(player);
	}
}
