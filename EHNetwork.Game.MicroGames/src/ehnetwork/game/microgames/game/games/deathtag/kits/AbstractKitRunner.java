package ehnetwork.game.microgames.game.games.deathtag.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public abstract class AbstractKitRunner extends Kit
{
	public AbstractKitRunner(MicroGamesManager manager, String name, KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		super(manager, name, kitAvailability, kitDesc, kitPerks, entityType, itemInHand);
	}

	public AbstractKitRunner(MicroGamesManager manager, String name, KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		super(manager, name, kitAvailability, cost, kitDesc, kitPerks, entityType, itemInHand);
	}
}
