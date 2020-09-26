package ehnetwork.game.arcade.game.games.murder.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitMurderer extends Kit
{
	public KitMurderer(ArcadeManager manager)
	{
		super(manager, "Murderer", KitAvailability.Hide, 0,

				new String[]
						{
								"KILL THE PLAYERS!"
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
		player.getInventory().setItem(0, ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
	}
}
