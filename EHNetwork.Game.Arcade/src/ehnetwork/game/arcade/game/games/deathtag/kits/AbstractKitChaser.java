package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public abstract class AbstractKitChaser extends Kit
{
	public AbstractKitChaser(ArcadeManager manager, String name, KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		super(manager, name, kitAvailability, kitDesc, kitPerks, entityType, itemInHand);
	}

	public AbstractKitChaser(ArcadeManager manager, String name, KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		super(manager, name, kitAvailability, cost, kitDesc, kitPerks, entityType, itemInHand);
	}
}
