package ehnetwork.core.benefit.benefits;

import org.bukkit.entity.Player;

import ehnetwork.core.benefit.BenefitManager;
import ehnetwork.core.benefit.BenefitManagerRepository;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.inventory.InventoryManager;

public class Thanksgiving2014 extends BenefitBase
{
	private InventoryManager _inventoryManager;
	
	public Thanksgiving2014(BenefitManager plugin, BenefitManagerRepository repository, InventoryManager inventoryManager)
	{
		super(plugin, "Thanksgiving2014", repository);
		
		_inventoryManager = inventoryManager;
	}

	@Override
	public void rewardPlayer(final Player player)
	{
		_inventoryManager.addItemToInventory(new Callback<Boolean>()
		{
			public void run(Boolean success)
			{
				if (success)
				{
					UtilPlayer.message(player, C.cPurple + C.Strike + "=============================================");
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, C.cGold + "HAPPY THANKSGIVING");
					UtilPlayer.message(player, "You received 1 Treasure Key!");
					UtilPlayer.message(player, "");
					UtilPlayer.message(player, C.cPurple + C.Strike + "=============================================");
				}
			}
		}, player, "Treasure", "Treasure Key", 1);
	}
}
