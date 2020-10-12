package ehnetwork.core.gadget.gadgets;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.visibility.VisibilityManager;

public class MorphPumpkinKing extends MorphGadget
{
	public MorphPumpkinKing(GadgetManager manager)
	{
		super(manager, "Pumpkin Kings Head", new String[] 
				{
				C.cWhite + "Transforms the wearer into",
				C.cWhite + "the dreaded Pumpkin King!",
				"",
				C.cYellow + "Earned by defeating the Pumpkin King",
				C.cYellow + "in the 2013 Halloween Horror Event.",
				},
				-1,
				Material.PUMPKIN, (byte)0);

	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		Disguise d1 = Manager.getDisguiseManager().createDisguise(EntityType.SKELETON);
		MobDisguise d2 = (MobDisguise) d1;
		d2.setCustomName(player.getName() + Manager.getClientManager().Get(player).GetRank());
		d2.setCustomNameVisible(true);
		Manager.getDisguiseManager().applyDisguise(d2, player);
		
		player.getInventory().setHelmet(new ItemStack(Material.JACK_O_LANTERN));

		VisibilityManager.Instance.setVisibility(player, false, UtilServer.getPlayers());
		VisibilityManager.Instance.setVisibility(player, true, UtilServer.getPlayers());
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
		player.getInventory().setHelmet(null);
	}

	
}
