package ehnetwork.game.arcade.game.games.micro.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;

public class KitArcher extends Kit
{
	public KitArcher(ArcadeManager manager)
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
