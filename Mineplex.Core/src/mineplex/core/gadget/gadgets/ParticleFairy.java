package mineplex.core.gadget.gadgets;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.gadget.types.ParticleGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;

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
