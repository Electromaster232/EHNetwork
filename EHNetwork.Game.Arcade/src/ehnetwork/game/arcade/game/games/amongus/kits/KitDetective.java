package ehnetwork.game.arcade.game.games.amongus.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitDetective extends Kit
{
	public KitDetective(ArcadeManager manager)
	{
		super(manager, "Detective", KitAvailability.Hide, 0,

				new String[]
						{
								"Shoot the murderer!"
						},

				new Perk[]
						{
						},
				EntityType.ZOMBIE,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(0, ItemStackFactory.Instance.CreateStack(Material.STONE_SWORD));
	}
}
