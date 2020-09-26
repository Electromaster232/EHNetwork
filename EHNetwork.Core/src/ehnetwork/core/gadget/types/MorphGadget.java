package ehnetwork.core.gadget.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.gadget.GadgetManager;

public abstract class MorphGadget extends Gadget
{
	public MorphGadget(GadgetManager manager, String name, String[] desc, int cost, Material mat, byte data)
	{
		super(manager, GadgetType.Morph, name, desc, cost, mat, data);
	}	
	
	public void ApplyArmor(Player player)
	{
		Manager.RemoveMorph(player);
		
		_active.add(player);
		
		UtilPlayer.message(player, F.main("Gadget", "You morphed into " + F.elem(GetName()) + "."));
	}
	
	public void RemoveArmor(Player player)
	{
		if (_active.remove(player))
			UtilPlayer.message(player, F.main("Gadget", "You unmorphed from " + F.elem(GetName()) + "."));
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent event)
	{
		Disable(event.getEntity());
	}
}
