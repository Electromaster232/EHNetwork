package nautilus.game.arcade.game.games.skywars.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitMiner extends Kit
{
	public KitMiner(ArcadeManager manager)
	{
		super(manager, "Miner", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Start with better tools!"
						}, 

						new Perk[] 
								{
						new PerkDigger(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_PICKAXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
		player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
	}
}
