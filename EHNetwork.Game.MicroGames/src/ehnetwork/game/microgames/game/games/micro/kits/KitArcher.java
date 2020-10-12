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
import ehnetwork.game.microgames.kit.perks.PerkFletcher;

public class KitArcher extends Kit
{
	public KitArcher(MicroGamesManager manager)
	{
		super(manager, "Archer", KitAvailability.Free, 

				new String[] 
						{
				"Shoot shoot!",
				" ",
				C.cGreen + "Wood Sword",
				C.cGreen + "Bow",
				C.cGreen + "3 Apples",
						}, 

						new Perk[] 
								{
					new PerkFletcher(20, 3, true)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WOOD_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.APPLE, 3));
	}
}
