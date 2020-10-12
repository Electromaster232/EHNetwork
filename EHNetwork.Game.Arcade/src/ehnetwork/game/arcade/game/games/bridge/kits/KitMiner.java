package ehnetwork.game.arcade.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDigger;
import ehnetwork.game.arcade.kit.perks.PerkOreFinder;

public class KitMiner extends Kit
{
	public KitMiner(ArcadeManager manager)
	{
		super(manager, "Miner", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Master of ore prospecting and digging quickly."
						}, 

						new Perk[] 
								{
				new PerkOreFinder(),
				new PerkDigger(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.STONE_PICKAXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_PICKAXE));
	}
}
