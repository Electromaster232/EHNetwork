package mineplex.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.disguise.disguises.DisguiseCow;
import mineplex.core.recharge.Recharge;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;

public class MorphCow extends MorphGadget
{	
	public MorphCow(GadgetManager manager)
	{
		super(manager, "Cow Morph", new String[] 
				{
				C.cWhite + "How now brown cow?",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Moo",
				},
				6000,
				Material.LEATHER, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseCow disguise = new DisguiseCow(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	@EventHandler
	public void Audio(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 2500, false, false))
			return;
		
		player.getWorld().playSound(player.getLocation(), Sound.COW_IDLE, 1f, 1f);
		
	}
}
