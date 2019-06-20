package nautilus.game.arcade.game.games.murder.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
