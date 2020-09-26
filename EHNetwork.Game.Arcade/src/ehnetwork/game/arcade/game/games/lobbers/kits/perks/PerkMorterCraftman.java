package ehnetwork.game.arcade.game.games.lobbers.kits.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkMorterCraftman extends Perk
{
	private Recharge _recharge;

	public PerkMorterCraftman()
	{
		super("Morter Craftman", new String[]
				{
				"You will recieve 1 mortar every " + C.cYellow + "10 Seconds.",
				"Maximum of 1."
				});
		
		_recharge = Recharge.Instance;
	}
	
	@EventHandler
	public void give(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (!Manager.GetGame().IsLive())
			return;
		
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			if (!_recharge.use(player, "Mortar Give", 10000, false, false))
				continue;
			
			//Has 1
			if (UtilInv.contains(player, Material.FIREBALL, (byte) 0, 1))
				continue;
			
			UtilInv.insert(player, new ItemBuilder(Material.FIREBALL).setTitle(F.item("Mortar")).build());
		}
	}
}
