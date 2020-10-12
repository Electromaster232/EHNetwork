package ehnetwork.game.microgames.game.games.micro.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitWorker extends Kit
{
	public KitWorker(MicroGamesManager manager)
	{
		super(manager, "Worker", KitAvailability.Free, 

				new String[] 
						{
				"DIG DIG!",
				" ",
				C.cGreen + "Wood Sword",
				C.cGreen + "Stone Spade",
				C.cGreen + "Stone Pickaxe",
				C.cGreen + "4 Apples",
						}, 

						new Perk[] 
								{

								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.STONE_PICKAXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WOOD_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_SPADE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_PICKAXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.APPLE, 4));
	}
}
