package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.recharge.Recharge;

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

		Disguise d1 = Manager.getDisguiseManager().createDisguise(EntityType.COW);
		MobDisguise d2 = (MobDisguise) d1;
		d2.setCustomName(player.getName() + Manager.getClientManager().Get(player).GetRank());
		d2.setCustomNameVisible(true);
		Manager.getDisguiseManager().applyDisguise(d2, player);
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

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 2500, false, false))
			return;
		
		player.getWorld().playSound(player.getLocation(), Sound.COW_IDLE, 1f, 1f);
		
	}
}
