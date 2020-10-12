package ehnetwork.game.microgames.game.games.runner.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkQuickshot;

public class KitArcher extends Kit
{
	public KitArcher(MicroGamesManager manager)
	{
		super(manager, "Archer", KitAvailability.Gem, 

				new String[] 
						{
				"Fire arrows to cause blocks to fall!"
						}, 

						new Perk[] 
								{ 
				new PerkQuickshot("Quickshot", 1.2, 6000)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
	}
}
