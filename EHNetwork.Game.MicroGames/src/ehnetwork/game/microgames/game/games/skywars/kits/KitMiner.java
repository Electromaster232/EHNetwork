package ehnetwork.game.microgames.game.games.skywars.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDigger;

public class KitMiner extends Kit
{
	public KitMiner(MicroGamesManager manager)
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
